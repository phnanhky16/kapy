package com.KapybaraWeb.kapyweb.dto.request.flower;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlowerCreateRequest {
    String flowerName;
    String flowerDescription;
    Long flowerQuantity;
    Boolean flowerStatus;
    Long priceId;
    Long quantityInStockId;
    List<Long> categoryId;
}
