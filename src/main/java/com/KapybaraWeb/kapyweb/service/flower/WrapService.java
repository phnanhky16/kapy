package com.KapybaraWeb.kapyweb.service.flower;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.request.flower.WrapCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.WrapUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.WrapResponse;
import com.KapybaraWeb.kapyweb.entity.*;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.*;
import com.KapybaraWeb.kapyweb.service.config.ImagesService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class WrapService {
    WrapRepository wrapRepository;
    ImagesService imagesService;
    PriceRepository priceRepository;
    QuantityInStockRepository quantityInStockRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    public WrapResponse createWrap(WrapCreateRequest request, MultipartFile wrapImage) {
        String imageUrl;
        try {
            imageUrl = (wrapImage != null && !wrapImage.isEmpty() ? imagesService.uploadImage(wrapImage) : null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Price p = priceRepository
                .findById(request.getPriceId())
                .orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));

        QuantityInStock quantityInStock = quantityInStockRepository
                .findById(request.getQuantityInStockId())
                .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));

        List<Long> categoryIds = request.getCategoryId();
        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        Wrap wrap = Wrap.builder()
                .wrapName(request.getWrapName())
                .wrapPrice(p.getPrice() * request.getWrapQuantity())
                .wrapImage(imageUrl)
                .wrapQuantity(request.getWrapQuantity())
                .price(p)
                .quantityInStock(quantityInStock)
                .categories(categories)
                .createdAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
        Wrap saved = wrapRepository.save(wrap);
        p.getWraps().add(saved);
        priceRepository.save(p);
        quantityInStock.getWraps().add(saved);
        quantityInStockRepository.save(quantityInStock);
        for (Category category : categories) {
            category.getWraps().add(saved);
            categoryRepository.save(category);
        }
        quantityInStockRepository.save(quantityInStock);
        return WrapResponse.from(saved);
    }

    public WrapResponse getWrapById(Long id) {
        Wrap w = wrapRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WRAP_NOT_FOUND));
        return WrapResponse.from(w);
    }

    public List<WrapResponse> getAllWraps() {
        return wrapRepository.findAll().stream().map(WrapResponse::from).collect(Collectors.toList());
    }

    public WrapResponse updateWrap(Long id, WrapUpdateRequest request, MultipartFile wrapImage) {
        User u = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Wrap wrap = wrapRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WRAP_NOT_FOUND));

        if (wrapImage != null && !wrapImage.isEmpty()) {
            try {
                String imageUrl = imagesService.uploadImage(wrapImage);
                wrap.setWrapImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (request.getQuantityInStockId() != null
                && !request.getQuantityInStockId()
                        .equals(wrap.getQuantityInStock().getQuantityInStockId())) {
            QuantityInStock oldStock = wrap.getQuantityInStock();
            QuantityInStock newStock = quantityInStockRepository
                    .findById(request.getQuantityInStockId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));
            oldStock.getWraps().remove(wrap);
            newStock.getWraps().add(wrap);
            wrap.setQuantityInStock(newStock);
            quantityInStockRepository.save(oldStock);
            quantityInStockRepository.save(newStock);
        }
        if (request.getPriceId() != null
                && !request.getPriceId().equals(wrap.getPrice().getPriceId())) {
            Price oldPrice = wrap.getPrice();
            Price newPrice = priceRepository
                    .findById(request.getPriceId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));
            oldPrice.getWraps().remove(wrap);
            newPrice.getWraps().add(wrap);
            wrap.setPrice(newPrice);
            priceRepository.save(oldPrice);
            priceRepository.save(newPrice);
        }
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            List<Long> categoryIds = request.getCategoryId();
            List<Category> newCategories = categoryRepository.findAllById(categoryIds);
            if (newCategories.size() != categoryIds.size()) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
            for (Category newCategory : newCategories) {
                newCategory.getWraps().remove(wrap);
                categoryRepository.save(newCategory);
            }
            for (Category oldCategory : wrap.getCategories()) {
                oldCategory.getWraps().remove(wrap);
                categoryRepository.save(oldCategory);
            }
            wrap.setCategories(newCategories);
        }
        wrap.setWrapName(request.getWrapName());
        wrap.setWrapQuantity(request.getWrapQuantity());
        if (!request.getWrapQuantity().equals(wrap.getWrapQuantity())) {
            wrap.setWrapPrice(wrap.getPrice().getPrice() * wrap.getWrapQuantity());
        }
        wrap.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        wrap.setUpdatedBy(u.getRole() + "-" + u.getFirstName());
        wrapRepository.save(wrap);
        return WrapResponse.from(wrap);
    }

    public String deActiveWrap(Long id) {
        User u = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Wrap w = wrapRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WRAP_NOT_FOUND));
        w.setWrapStatus(false);
        w.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        w.setUpdatedBy(u.getRole() + "-" + u.getFirstName());
        wrapRepository.save(w);
        return "DeActive wrap successfully";
    }

    public String activeWrap(Long id) {
        User u = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Wrap w = wrapRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WRAP_NOT_FOUND));
        w.setWrapStatus(false);
        w.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        w.setUpdatedBy(u.getRole() + "-" + u.getFirstName());
        wrapRepository.save(w);
        return "Active wrap successfully";
    }
}
