package com.KapybaraWeb.kapyweb.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResetPasswordRequest {
    @NotBlank(message = "PASSWORD_NOTBLANK")
    @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
    String newPassword;

    @NotBlank(message = "PASSWORD_NOTBLANK")
    @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
    String confirmNewPassword;
}
