package com.KapybaraWeb.kapyweb.dto.response.flower;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.KapybaraWeb.kapyweb.entity.Category;
import com.KapybaraWeb.kapyweb.entity.Wrap;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrapResponse {
    Long wrapId;
    String wrapName;
    Double wrapPrice;
    String wrapImage;
    Long wrapQuantity;
    Boolean wrapStatus;
    Long quantityInStock;
    List<String> categoryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;

    String updatedBy;

    public static WrapResponse from(Wrap wrap) {
        return WrapResponse.builder()
                .wrapId(wrap.getWrapId())
                .wrapName(wrap.getWrapName())
                .wrapPrice(wrap.getWrapPrice())
                .wrapImage(wrap.getWrapImage())
                .wrapQuantity(wrap.getWrapQuantity())
                .wrapStatus(wrap.getWrapStatus())
                .quantityInStock(wrap.getQuantityInStock().getQuantityInStock())
                .categoryName(
                        wrap.getCategories().stream().map(Category::getName).collect(Collectors.toList()))
                .createdAt(wrap.getCreatedAt())
                .updatedAt(wrap.getUpdatedAt())
                .updatedBy(wrap.getUpdatedBy())
                .build();
    }
}
