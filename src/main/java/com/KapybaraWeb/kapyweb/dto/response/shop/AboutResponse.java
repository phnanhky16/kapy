package com.KapybaraWeb.kapyweb.dto.response.shop;

import com.KapybaraWeb.kapyweb.entity.About;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutResponse {
    Long aboutId;
    String aboutImage;
    String aboutText;
    Boolean aboutStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;
    String createdBy;

    String updatedBy;
    public static AboutResponse from(About about) {
        return AboutResponse.builder()
                .aboutId(about.getAboutId())
                .aboutImage(about.getAboutImage())
                .aboutText(about.getAboutText())
                .aboutStatus(about.getAboutStatus())
                .createdAt(about.getCreatedAt())
                .createdBy(about.getCreatedBy())
                .updatedAt(about.getUpdatedAt())
                .updatedBy(about.getUpdatedBy())
                .build();
    }
}
