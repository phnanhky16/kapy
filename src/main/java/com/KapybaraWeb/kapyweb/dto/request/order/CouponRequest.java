package com.KapybaraWeb.kapyweb.dto.request.order;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponRequest {
    String couponName;
    String couponCode;
    Long percent;
    Long couponQuantity;
    LocalDate beginDate;
    LocalDate endDate;
    Boolean status;
}
