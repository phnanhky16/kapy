package com.KapybaraWeb.kapyweb.dto.response.flower;

import java.time.LocalDate;
import java.util.List;

import com.KapybaraWeb.kapyweb.entity.Flower;
import com.KapybaraWeb.kapyweb.entity.QuantityInStock;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuantityInStockResponse {
    Long quantityInStockId;
    String name;
    Long QuantityInStock;
    List<Flower> flowers;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;

    String updatedBy;

    public static QuantityInStockResponse from(QuantityInStock quantityInStock) {
        return QuantityInStockResponse.builder()
                .quantityInStockId(quantityInStock.getQuantityInStockId())
                .name(quantityInStock.getName())
                .QuantityInStock(quantityInStock.getQuantityInStock())
                .flowers(quantityInStock.getFlowers())
                .createdAt(quantityInStock.getCreatedAt())
                .updatedAt(quantityInStock.getUpdatedAt())
                .updatedBy(quantityInStock.getUpdatedBy())
                .build();
    }
}
