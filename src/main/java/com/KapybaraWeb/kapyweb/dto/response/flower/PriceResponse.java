package com.KapybaraWeb.kapyweb.dto.response.flower;

import java.time.LocalDate;
import java.util.List;

import com.KapybaraWeb.kapyweb.entity.Flower;
import com.KapybaraWeb.kapyweb.entity.Price;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceResponse {
    Long priceId;
    String name;
    Double price;
    List<Flower> flowers;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;

    String updatedBy;

    public static PriceResponse from(Price price) {
        return PriceResponse.builder()
                .priceId(price.getPriceId())
                .name(price.getName())
                .price(price.getPrice())
                .flowers(price.getFlowers())
                .createdAt(price.getCreatedAt())
                .updatedAt(price.getUpdatedAt())
                .updatedBy(price.getUpdatedBy())
                .build();
    }
}
