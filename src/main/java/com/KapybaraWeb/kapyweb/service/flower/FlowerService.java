package com.KapybaraWeb.kapyweb.service.flower;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.request.flower.FlowerCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.FlowerUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.FlowerResponse;
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
public class FlowerService {
    FlowerRepository flowerRepository;
    ImagesService imagesService;
    PriceRepository priceRepository;
    QuantityInStockRepository quantityInStockRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    public FlowerResponse createFlower(FlowerCreateRequest request, MultipartFile flowerImage) {
        String imageUrl;
        try {
            imageUrl = (flowerImage != null && !flowerImage.isEmpty()) ? imagesService.uploadImage(flowerImage) : null;
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

        Flower f = Flower.builder()
                .flowerName(request.getFlowerName())
                .flowerPrice(request.getFlowerQuantity() * p.getPrice())
                .flowerDescription(request.getFlowerDescription())
                .flowerImage(imageUrl)
                .flowerQuantity(request.getFlowerQuantity())
                .flowerStatus(request.getFlowerStatus())
                .price(p)
                .quantityInStock(quantityInStock)
                .categories(categories)
                .createdAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
        Flower saved = flowerRepository.save(f);

        p.getFlowers().add(saved);
        priceRepository.save(p);

        quantityInStock.getFlowers().add(saved);
        quantityInStockRepository.save(quantityInStock);

        for (Category category : categories) {
            category.getFlowers().add(saved);
            categoryRepository.save(category);
        }
        return FlowerResponse.from(saved);
    }

    public FlowerResponse getFlowerById(Long id) {
        Flower flower = flowerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FLOWER_NOT_FOUND));
        return FlowerResponse.from(flower);
    }

    public List<FlowerResponse> getAllFlowers() {
        return flowerRepository.findAll().stream().map(FlowerResponse::from).collect(Collectors.toList());
    }

    public FlowerResponse updateFlower(Long id, FlowerUpdateRequest request, MultipartFile flowerImage) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Flower flower = flowerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FLOWER_NOT_FOUND));

        // Upload image if provided
        if (flowerImage != null && !flowerImage.isEmpty()) {
            try {
                String imageUrl = imagesService.uploadImage(flowerImage);
                flower.setFlowerImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Update QuantityInStock if changed
        if (request.getQuantityInStockId() != null
                && !request.getQuantityInStockId()
                        .equals(flower.getQuantityInStock().getQuantityInStockId())) {

            QuantityInStock oldStock = flower.getQuantityInStock();
            QuantityInStock newStock = quantityInStockRepository
                    .findById(request.getQuantityInStockId())
                    .orElseThrow(() -> new AppException(ErrorCode.QUANTITY_NOT_FOUND));

            oldStock.getFlowers().remove(flower);
            newStock.getFlowers().add(flower);

            flower.setQuantityInStock(newStock);

            quantityInStockRepository.save(oldStock);
            quantityInStockRepository.save(newStock);
        }

        // Update Price if changed
        if (request.getPriceId() != null
                && !request.getPriceId().equals(flower.getPrice().getPriceId())) {

            Price oldPrice = flower.getPrice();
            Price newPrice = priceRepository
                    .findById(request.getPriceId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRICE_NOT_FOUND));

            oldPrice.getFlowers().remove(flower);
            newPrice.getFlowers().add(flower);

            flower.setPrice(newPrice);

            priceRepository.save(oldPrice);
            priceRepository.save(newPrice);
        }

        // Update Categories if new list is not empty
        if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
            List<Long> categoryIds = request.getCategoryId();
            List<Category> newCategories = categoryRepository.findAllById(categoryIds);

            if (newCategories.size() != categoryIds.size()) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }

            // Remove flower from old categories
            for (Category oldCategory : flower.getCategories()) {
                oldCategory.getFlowers().remove(flower);
                categoryRepository.save(oldCategory);
            }

            // Add flower to new categories
            for (Category newCategory : newCategories) {
                newCategory.getFlowers().add(flower);
                categoryRepository.save(newCategory);
            }

            flower.setCategories(newCategories);
        }
        flower.setFlowerName(request.getFlowerName());
        flower.setFlowerDescription(request.getFlowerDescription());
        flower.setFlowerQuantity(request.getFlowerQuantity());
        flower.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        flower.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        if (!request.getFlowerQuantity().equals(flower.getFlowerQuantity())) {
            flower.setFlowerPrice(
                    request.getFlowerQuantity() * flower.getPrice().getPrice());
        }
        flowerRepository.save(flower);

        return FlowerResponse.from(flower);
    }

    public String deActiveFlower(Long id) {
        User user = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Flower flower = flowerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FLOWER_NOT_FOUND));
        flower.setFlowerStatus(false);
        flower.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        flower.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        flowerRepository.save(flower);
        return "DeActive flower successfully";
    }

    public String activeFlower(Long id) {
        User user = userRepository
                .findByUsername(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Flower flower = flowerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.FLOWER_NOT_FOUND));
        flower.setFlowerStatus(true);
        flower.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        flower.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
        flowerRepository.save(flower);
        return "Active flower successfully";
    }
}
