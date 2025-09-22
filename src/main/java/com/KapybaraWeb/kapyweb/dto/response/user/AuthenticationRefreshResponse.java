package com.KapybaraWeb.kapyweb.dto.response.user;

import com.KapybaraWeb.kapyweb.enums.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRefreshResponse {
    String token;
    Role role;
    Boolean success;
}
