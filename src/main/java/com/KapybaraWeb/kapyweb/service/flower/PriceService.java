package com.KapybaraWeb.kapyweb.service.flower;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.KapybaraWeb.kapyweb.dto.request.flower.PriceRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.PriceResponse;
import com.KapybaraWeb.kapyweb.entity.Flower;
import com.KapybaraWeb.kapyweb.entity.Price;
import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.PriceRepository;
import com.KapybaraWeb.kapyweb.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceService {
    PriceRepository priceRepository;
    private final UserRepository userRepository;

    public PriceResponse createPrice(PriceRequest request) {
        Price price = Price.builder()
                .name(request.getName())
                .price(request.getPrice())
                .createdAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
        priceRepository.save(price);
        return PriceResponse.from(price);
    }

    public PriceResponse getPriceById(Long id) {
        Price price = priceRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));
        return PriceResponse.from(price);
    }

    public List<PriceResponse> getAllPrice() {
        return priceRepository.findAll().stream().map(PriceResponse::from).toList();
    }

    public PriceResponse updatePrice(Long id, PriceRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Price price = priceRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));
        price.setName(request.getName());
        price.setPrice(request.getPrice());
        price.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        price.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        priceRepository.save(price);
        return PriceResponse.from(price);
    }

    public void deletePrice(Long id) {
        Price price = priceRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));
        List<Flower> flowers = price.getFlowers();
        if (!flowers.isEmpty()) {
            throw new AppException(ErrorCode.PRICE_HAS_FLOWERS);
        }
        priceRepository.delete(price);
    }
}
