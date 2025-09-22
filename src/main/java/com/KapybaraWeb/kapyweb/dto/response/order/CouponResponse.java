package com.KapybaraWeb.kapyweb.dto.response.order;

import java.time.LocalDate;

import com.KapybaraWeb.kapyweb.entity.Coupon;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponse {
    Long couponId;
    String couponName;
    String couponCode;
    Long percent;
    Long couponQuantity;
    LocalDate beginDate;
    LocalDate endDate;
    Boolean status;
    LocalDate create_at;
    LocalDate update_at;
    String update_by;

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .couponId(coupon.getCouponId())
                .couponName(coupon.getCouponName())
                .couponCode(coupon.getCouponCode())
                .percent(coupon.getPercent())
                .couponQuantity(coupon.getCouponQuantity())
                .endDate(coupon.getEndDate())
                .beginDate(coupon.getBeginDate())
                .status(coupon.getStatus())
                .create_at(coupon.getCreate_at())
                .update_at(coupon.getUpdate_at())
                .update_by(coupon.getUpdate_by())
                .build();
    }
}
