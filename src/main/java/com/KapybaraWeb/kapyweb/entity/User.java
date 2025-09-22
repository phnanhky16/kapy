package com.KapybaraWeb.kapyweb.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import org.hibernate.annotations.Nationalized;

import com.KapybaraWeb.kapyweb.enums.Gender;
import com.KapybaraWeb.kapyweb.enums.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(50) COLLATE utf8mb4_unicode_ci")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "first_name")
    @Nationalized
    String firstName;

    @Column(name = "last_name")
    @Nationalized
    String lastName;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "birthday")
    LocalDate birthday;

    @Column(name = "gender")
    Gender gender;

    @Column(name = "status")
    Boolean status;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "updated_at")
    LocalDate updatedAt;

    @Column(name = "created_by")
    String updatedBy;
}
