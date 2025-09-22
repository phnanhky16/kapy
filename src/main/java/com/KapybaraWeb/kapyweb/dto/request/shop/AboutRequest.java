package com.KapybaraWeb.kapyweb.dto.request.shop;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutRequest {
    String aboutText;
    Boolean aboutStatus;
}
