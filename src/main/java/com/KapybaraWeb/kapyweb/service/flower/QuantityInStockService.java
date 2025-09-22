package com.KapybaraWeb.kapyweb.service.flower;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.KapybaraWeb.kapyweb.dto.request.flower.QuantityInStockRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.QuantityInStockResponse;
import com.KapybaraWeb.kapyweb.entity.Flower;
import com.KapybaraWeb.kapyweb.entity.QuantityInStock;
import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.FlowerRepository;
import com.KapybaraWeb.kapyweb.repository.QuantityInStockRepository;
import com.KapybaraWeb.kapyweb.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuantityInStockService {
    QuantityInStockRepository quantityInStockRepository;
    FlowerRepository flowerRepository;
    UserRepository userRepository;

    public QuantityInStockResponse createQuantityInStock(QuantityInStockRequest request) {
        QuantityInStock quantityInStock = new QuantityInStock();
        quantityInStock.setName(request.getName());
        quantityInStock.setQuantityInStock(request.getQuantityInStock());
        quantityInStock.setCreatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        quantityInStock = quantityInStockRepository.save(quantityInStock);
        return QuantityInStockResponse.from(quantityInStock);
    }

    public List<QuantityInStockResponse> getAllQuantityInStock() {
        return quantityInStockRepository.findAll().stream()
                .map(QuantityInStockResponse::from)
                .toList();
    }

    public QuantityInStockResponse getQuantityInStockById(Long id) {
        QuantityInStock quantityInStock = quantityInStockRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));
        return QuantityInStockResponse.from(quantityInStock);
    }

    public QuantityInStockResponse updateQuantityInStock(Long id, QuantityInStockRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        QuantityInStock quantityInStock = quantityInStockRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));

        quantityInStock.setName(request.getName());
        quantityInStock.setQuantityInStock(request.getQuantityInStock());
        quantityInStock.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        quantityInStock.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        quantityInStock = quantityInStockRepository.save(quantityInStock);
        return QuantityInStockResponse.from(quantityInStock);
    }

    public void deleteQuantityInStock(Long id) {
        QuantityInStock quantityInStock = quantityInStockRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));
        List<Flower> flowers = quantityInStock.getFlowers();
        if (!flowers.isEmpty()) {
            throw new AppException(ErrorCode.QUANTITY_HAS_FLOWERS);
        }
        quantityInStockRepository.delete(quantityInStock);
    }
}
