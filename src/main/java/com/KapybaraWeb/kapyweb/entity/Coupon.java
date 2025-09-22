package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import org.hibernate.annotations.Nationalized;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "kapyflower_coupon")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long couponId;

    @Column(name = "name")
    @Nationalized
    String couponName;

    @Column(name = "code")
    String couponCode;

    @Column(name = "percent")
    Long percent;

    @Column(name = "quantity")
    Long couponQuantity;

    @Column(name = "begin_date")
    LocalDate beginDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "status")
    Boolean status;

    @Column(name = "create_at")
    LocalDate create_at;

    @Column(name = "update_at")
    LocalDate update_at;

    @Column(name = "update_by")
    String update_by;
}
