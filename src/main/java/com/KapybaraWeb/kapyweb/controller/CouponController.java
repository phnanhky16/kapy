package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.order.CouponRequest;
import com.KapybaraWeb.kapyweb.dto.response.order.CouponResponse;
import com.KapybaraWeb.kapyweb.service.order.CouponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/create")
    public ApiResponse<CouponResponse> createCoupon(@RequestBody CouponRequest couponRequest) {
        return ApiResponse.<CouponResponse>builder()
                .message("Create coupon successfully")
                .result(couponService.createCoupon(couponRequest))
                .success(true)
                .build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<CouponResponse> updateCoupon(@PathVariable Long id, @RequestBody CouponRequest couponRequest) {
        return ApiResponse.<CouponResponse>builder()
                .message("Update coupon successfully")
                .result(couponService.updateCoupon(id, couponRequest))
                .success(true)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteCoupon(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .result(couponService.deleteCoupon(id))
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CouponResponse> getCoupon(@PathVariable Long id) {
        return ApiResponse.<CouponResponse>builder()
                .message("Get coupon successfully")
                .result(couponService.getCoupon(id))
                .success(true)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CouponResponse>> getAllCoupon() {
        return ApiResponse.<List<CouponResponse>>builder()
                .message("Get all coupon successfully")
                .result(couponService.getCoupons())
                .success(true)
                .build();
    }
}
