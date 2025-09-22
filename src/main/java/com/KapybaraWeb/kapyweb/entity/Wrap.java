package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.Nationalized;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kapyflower_wrap")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long wrapId;

    @Column(name = "name")
    @Nationalized
    String wrapName;

    @Column(name = "price")
    Double wrapPrice;

    @Column(name = "image")
    String wrapImage;

    @Column(name = "quantity")
    Long wrapQuantity;

    @Column(name = "status")
    Boolean wrapStatus;

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
            name = "wrap_category",
            joinColumns = @JoinColumn(name = "wrap_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    List<Category> categories;
}
