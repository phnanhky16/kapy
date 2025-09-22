package com.KapybaraWeb.kapyweb.service.order;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.KapybaraWeb.kapyweb.dto.request.order.CouponRequest;
import com.KapybaraWeb.kapyweb.dto.response.order.CouponResponse;
import com.KapybaraWeb.kapyweb.entity.Coupon;
import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.CouponRepository;
import com.KapybaraWeb.kapyweb.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponService {
    UserRepository userRepository;
    CouponRepository couponRepository;

    public CouponResponse createCoupon(CouponRequest couponRequest) {
        if (!couponRepository.existsCouponByCouponCode(couponRequest.getCouponCode())) {
            throw new AppException(ErrorCode.COUPON_CODE_EXISTED);
        }
        Coupon coupon = Coupon.builder()
                .couponName(couponRequest.getCouponName())
                .couponCode(couponRequest.getCouponCode())
                .couponQuantity(couponRequest.getCouponQuantity())
                .percent(couponRequest.getPercent())
                .beginDate(couponRequest.getBeginDate())
                .endDate(couponRequest.getEndDate())
                .status(couponRequest.getStatus())
                .create_at(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh)")))
                .build();
        couponRepository.save(coupon);
        return CouponResponse.from(coupon);
    }

    public CouponResponse updateCoupon(Long id, CouponRequest couponRequest) {
        User u = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
        coupon.setCouponName(couponRequest.getCouponName());
        coupon.setCouponCode(couponRequest.getCouponCode());
        coupon.setPercent(couponRequest.getPercent());
        coupon.setCouponQuantity(couponRequest.getCouponQuantity());
        coupon.setBeginDate(couponRequest.getBeginDate());
        coupon.setEndDate(couponRequest.getEndDate());
        coupon.setStatus(couponRequest.getStatus());
        coupon.setUpdate_at(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh)")));
        coupon.setUpdate_by(u.getRole() + "-" + u.getFirstName());
        couponRepository.save(coupon);
        return CouponResponse.from(coupon);
    }

    public String deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
        couponRepository.delete(coupon);
        return "Deleted coupon successfully";
    }

    public CouponResponse getCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
        return CouponResponse.from(coupon);
    }

    public List<CouponResponse> getCoupons() {
        return couponRepository.findAll().stream().map(CouponResponse::from).collect(Collectors.toList());
    }
}
