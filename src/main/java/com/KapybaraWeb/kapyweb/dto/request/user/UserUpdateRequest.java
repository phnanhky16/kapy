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
public class UserUpdateRequest {
    @NotBlank(message = "FIRST_NAME_NOTBLANK")
    String firstName;

    @NotBlank(message = "LAST_NAME_NOTBLANK")
    String lastName;

    @NotBlank(message = "EMAIL_NOTBLANK")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "PHONE_NUMBER_NOTBLANK")
    @Pattern(regexp = "^(0[3|5|7|8|9])[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    String phoneNumber;

    @Past(message = "BIRTHDAY_INVALID")
    LocalDate birthday;

    @NotNull(message = "GENDER_NOTBLANK")
    Gender gender;
}
