package com.KapybaraWeb.kapyweb.dto.response.flower;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.KapybaraWeb.kapyweb.entity.Category;
import com.KapybaraWeb.kapyweb.entity.Flower;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlowerResponse {
    Long flowerId;
    String flowerName;
    String flowerDescription;
    Long flowerQuantity;
    String flowerImage;
    Boolean flowerStatus;
    Double flowerPrice;
    Long quantityInStock;
    List<String> categoryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;

    String updatedBy;

    public static FlowerResponse from(Flower flower) {
        return FlowerResponse.builder()
                .flowerId(flower.getFlowerId())
                .flowerName(flower.getFlowerName())
                .flowerDescription(flower.getFlowerDescription())
                .flowerQuantity(flower.getFlowerQuantity())
                .flowerImage(flower.getFlowerImage())
                .flowerStatus(flower.getFlowerStatus())
                .flowerPrice(flower.getFlowerPrice())
                .quantityInStock(flower.getQuantityInStock().getQuantityInStock())
                .categoryName(
                        flower.getCategories().stream().map(Category::getName).collect(Collectors.toList()))
                .createdAt(flower.getCreatedAt())
                .updatedAt(flower.getUpdatedAt())
                .updatedBy(flower.getUpdatedBy())
                .build();
    }
}
