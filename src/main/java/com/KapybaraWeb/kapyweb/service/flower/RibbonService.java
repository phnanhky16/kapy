package com.KapybaraWeb.kapyweb.service.flower;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.request.flower.RibbonCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.RibbonUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.RibbonResponse;
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
public class RibbonService {
    RibbonRepository ribbonRepository;
    ImagesService imagesService;
    PriceRepository priceRepository;
    QuantityInStockRepository quantityInStockRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    public RibbonResponse createRibbon(RibbonCreateRequest request, MultipartFile ribbonImage) {
        String imageUrl;
        try {
            imageUrl = (ribbonImage != null && !ribbonImage.isEmpty() ? imagesService.uploadImage(ribbonImage) : null);
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
        Ribbon ribbon = Ribbon.builder()
                .ribbonName(request.getRibbonName())
                .ribbonPrice(p.getPrice() * request.getRibbonQuantity())
                .ribbonImage(imageUrl)
                .ribbonQuantity(request.getRibbonQuantity())
                .price(p)
                .quantityInStock(quantityInStock)
                .categories(categories)
                .createdAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
        Ribbon saved = ribbonRepository.save(ribbon);
        p.getRibbons().add(saved);
        priceRepository.save(p);
        quantityInStock.getRibbons().add(saved);
        quantityInStockRepository.save(quantityInStock);
        for (Category category : categories) {
            category.getRibbons().add(saved);
            categoryRepository.save(category);
        }
        quantityInStockRepository.save(quantityInStock);
        return RibbonResponse.from(saved);
    }

    public RibbonResponse getRibbonById(Long id) {
        Ribbon r = ribbonRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIBBON_NOT_FOUND));
        return RibbonResponse.from(r);
    }

    public List<RibbonResponse> getAllRibbons() {
        return ribbonRepository.findAll().stream().map(RibbonResponse::from).collect(Collectors.toList());
    }

    public RibbonResponse updateRibbon(Long id, RibbonUpdateRequest request, MultipartFile ribbonImage) {
        User u = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Ribbon ribbon = ribbonRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIBBON_NOT_FOUND));

        if (ribbonImage != null && !ribbonImage.isEmpty()) {
            try {
                String imageUrl = imagesService.uploadImage(ribbonImage);
                ribbon.setRibbonImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (request.getQuantityInStockId() != null
                && !request.getQuantityInStockId()
                        .equals(ribbon.getQuantityInStock().getQuantityInStockId())) {
            QuantityInStock oldStock = ribbon.getQuantityInStock();
            QuantityInStock newStock = quantityInStockRepository
                    .findById(request.getQuantityInStockId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));
            oldStock.getRibbons().remove(ribbon);
            newStock.getRibbons().add(ribbon);
            ribbon.setQuantityInStock(newStock);
            quantityInStockRepository.save(oldStock);
            quantityInStockRepository.save(newStock);
        }
        if (request.getPriceId() != null
                && !request.getPriceId().equals(ribbon.getPrice().getPriceId())) {
            Price oldPrice = ribbon.getPrice();
            Price newPrice = priceRepository
                    .findById(request.getPriceId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));
            oldPrice.getRibbons().remove(ribbon);
            newPrice.getRibbons().add(ribbon);
            ribbon.setPrice(newPrice);
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
                newCategory.getRibbons().remove(ribbon);
                categoryRepository.save(newCategory);
            }
            for (Category oldCategory : ribbon.getCategories()) {
                oldCategory.getRibbons().remove(ribbon);
                categoryRepository.save(oldCategory);
            }
            ribbon.setCategories(newCategories);
        }
        ribbon.setRibbonName(request.getRibbonName());
        ribbon.setRibbonQuantity(request.getRibbonQuantity());
        if (!request.getRibbonQuantity().equals(ribbon.getRibbonQuantity())) {
            ribbon.setRibbonPrice(ribbon.getPrice().getPrice() * ribbon.getRibbonQuantity());
        }
        ribbon.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        ribbon.setUpdatedBy(u.getRole() + "-" + u.getFirstName());
        ribbonRepository.save(ribbon);
        return RibbonResponse.from(ribbon);
    }

    public String deActiveRibbon(Long id) {
        User u = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Ribbon r = ribbonRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIBBON_NOT_FOUND));
        r.setRibbonStatus(false);
        r.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        r.setUpdatedBy(u.getRole() + "-" + u.getFirstName());
        ribbonRepository.save(r);
        return "DeActive ribbon successfully";
    }

    public String activeRibbon(Long id) {
        User u = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Ribbon r = ribbonRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIBBON_NOT_FOUND));
        r.setRibbonStatus(false);
        r.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        r.setUpdatedBy(u.getRole() + "-" + u.getFirstName());
        ribbonRepository.save(r);
        return "Active ribbon successfully";
    }
}
