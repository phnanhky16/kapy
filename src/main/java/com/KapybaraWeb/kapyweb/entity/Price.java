package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.Nationalized;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kapyflower_price")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    Long priceId;

    @Column(name = "name")
    @Nationalized
    String name;

    @Column(name = "price")
    Double price;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "update_at")
    LocalDate updatedAt;

    @Column(name = "update_by")
    String updatedBy;

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Flower> flowers = new ArrayList<>();

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Ribbon> ribbons = new ArrayList<>();

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Wrap> wraps = new ArrayList<>();

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Note> notes = new ArrayList<>();
}
