package com.KapybaraWeb.kapyweb.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "about")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long aboutId;

    @Column(name = "image")
    String aboutImage;

    @Column(name = "text", columnDefinition = "TEXT")
    String aboutText;

    @Column(name = "status")
    Boolean aboutStatus;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "updated_at")
    LocalDate updatedAt;

    @Column(name = "updated_by")
    String updatedBy;
}
