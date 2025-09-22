package com.KapybaraWeb.kapyweb.dto.request.flower;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    @NotBlank(message = "NAME_NOT_BLANK")
    String name;

    @Pattern(regexp = "\\d{2}-\\d{2}", message = "DATE_MUST_BE_IN_dd_MM")
    String beginDate;

    @Pattern(regexp = "\\d{2}-\\d{2}", message = "DATE_MUST_BE_IN_dd_MM")
    String endDate;
}
