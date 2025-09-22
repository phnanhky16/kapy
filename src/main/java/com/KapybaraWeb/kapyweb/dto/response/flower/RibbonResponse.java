package com.KapybaraWeb.kapyweb.dto.response.flower;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.KapybaraWeb.kapyweb.entity.Category;
import com.KapybaraWeb.kapyweb.entity.Ribbon;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RibbonResponse {
    Long ribbonId;
    String ribbonName;
    Double ribbonPrice;
    String ribbonImage;
    Long ribbonQuantity;
    Boolean ribbonStatus;
    Long quantityInStock;
    List<String> categoryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;

    String updatedBy;

    public static RibbonResponse from(Ribbon ribbon) {
        return RibbonResponse.builder()
                .ribbonId(ribbon.getRibbonId())
                .ribbonName(ribbon.getRibbonName())
                .ribbonQuantity(ribbon.getRibbonQuantity())
                .ribbonImage(ribbon.getRibbonImage())
                .ribbonStatus(ribbon.getRibbonStatus())
                .ribbonPrice(ribbon.getRibbonPrice())
                .quantityInStock(ribbon.getQuantityInStock().getQuantityInStock())
                .categoryName(
                        ribbon.getCategories().stream().map(Category::getName).collect(Collectors.toList()))
                .createdAt(ribbon.getCreatedAt())
                .updatedAt(ribbon.getUpdatedAt())
                .updatedBy(ribbon.getUpdatedBy())
                .build();
    }
}
