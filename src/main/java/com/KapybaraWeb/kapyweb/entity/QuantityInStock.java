package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.Nationalized;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kapyflower_QuanityInstock")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityInStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long quantityInStockId;

    @Column(name = "name")
    @Nationalized
    String name;

    @Column(name = "quantity_in_stock")
    Long quantityInStock;

    @Column(name = "created_at")
    LocalDate CreatedAt;

    @Column(name = "updated_at")
    LocalDate UpdatedAt;

    @Column(name = "updated_by")
    String UpdatedBy;

    @OneToMany(mappedBy = "quantityInStock", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Flower> flowers = new ArrayList<>();

    @OneToMany(mappedBy = "quantityInStock", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Ribbon> ribbons = new ArrayList<>();

    @OneToMany(mappedBy = "quantityInStock", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Wrap> wraps = new ArrayList<>();

    @OneToMany(mappedBy = "quantityInStock", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Note> notes = new ArrayList<>();
}
