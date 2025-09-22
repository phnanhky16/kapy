package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.Nationalized;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kapyflower_flower")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long flowerId;

    @Column(name = "name")
    @Nationalized
    String flowerName;

    @Column(name = "price")
    Double flowerPrice;

    @Column(name = "description")
    @Nationalized
    String flowerDescription;

    @Column(name = "image")
    String flowerImage;

    @Column(name = "quantity")
    Long flowerQuantity;

    @Column(name = "status")
    Boolean flowerStatus;

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
            name = "flower_category",
            joinColumns = @JoinColumn(name = "flower_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    List<Category> categories;
}
