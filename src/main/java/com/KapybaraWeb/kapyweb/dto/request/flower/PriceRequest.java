package com.KapybaraWeb.kapyweb.dto.request.flower;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceRequest {
    @NotBlank(message = "NAME_NOTBLANK")
    String name;

    @NotNull(message = "PRICE_NOTNULL")
    @Min(value = 0, message = "PRICE_MIN")
    Double price;
}
