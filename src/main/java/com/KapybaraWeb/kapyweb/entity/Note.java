package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.Nationalized;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kapyflower_note")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long noteId;

    @Column(name = "name")
    @Nationalized
    String noteName;

    @Column(name = "price")
    Double notePrice;

    @Column(name = "text")
    @Nationalized
    String noteText;

    @Column(name = "image")
    String noteImage;

    @Column(name = "quantity")
    Long noteQuantity;

    @Column(name = "status")
    Boolean noteStatus;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "updated_at")
    LocalDate updatedAt;

    @Column(name = "updated_by")
    String updatedBy;

    @ManyToOne
    @JoinColumn(name = "price_id", nullable = false)
    Price price;

    @ManyToOne
    @JoinColumn(name = "quantity_in_stock_id", nullable = false)
    QuantityInStock quantityInStock;

    @ManyToMany
    @JoinTable(
            name = "note_category",
            joinColumns = @JoinColumn(name = "flower_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    List<Category> categories;
}
