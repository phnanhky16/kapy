package com.KapybaraWeb.kapyweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KapybaraWeb.kapyweb.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsCouponByCouponCode(String couponCode);
}
