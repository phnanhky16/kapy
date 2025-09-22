package com.KapybaraWeb.kapyweb.dto.response.user;

import java.time.LocalDate;
import java.util.List;

import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.enums.Gender;
import com.KapybaraWeb.kapyweb.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long userId;
    String username;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate birthday;

    Gender gender;
    Boolean status;
    Role role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;

    String updatedBy;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .status(user.getStatus())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy() + "-" + user.getFirstName())
                .build();
    }

    public static List<UserResponse> fromUsers(List<User> users) {
        return users.stream().map(UserResponse::fromUser).toList();
    }
}
