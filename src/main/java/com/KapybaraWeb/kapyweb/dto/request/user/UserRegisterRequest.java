package com.KapybaraWeb.kapyweb.dto.request.user;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

import com.KapybaraWeb.kapyweb.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterRequest {
    @NotBlank(message = "USERNAME_NOTBLANK")
    @Size(min = 4, max = 30, message = "USERNAME_INVALID")
    String username;

    @NotBlank(message = "PASSWORD_NOTBLANK")
    @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "FIRST_NAME_NOTBLANK")
    String firstName;

    @NotBlank(message = "LAST_NAME_NOTBLANK")
    String lastName;

    @NotBlank(message = "EMAIL_NOTBLANK")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "PHONE_NUMBER_NOTBLANK")
    @Pattern(regexp = "^(84|0[35789])\\d{8}$", message = "PHONE_NUMBER_INVALID")
    String phoneNumber;

    @Past(message = "BIRTHDAY_INVALID")
    LocalDate birthday;

    @NotNull(message = "GENDER_NOTBLANK")
    Gender gender;
}
