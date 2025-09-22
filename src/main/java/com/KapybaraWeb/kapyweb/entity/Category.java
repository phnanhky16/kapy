package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kapyflower_Category")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long categoryId;

    @Column(name = "name")
    String name;

    @Column(name = "begin_date")
    String beginDate;

    @Column(name = "end_date")
    String endDate;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "udpated_at")
    LocalDate updatedAt;

    @Column(name = "updated_by")
    String updatedBy;

    @ManyToMany(mappedBy = "categories")
    List<Flower> flowers = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    List<Ribbon> ribbons = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    List<Wrap> wraps = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    List<Note> notes = new ArrayList<>();
}
