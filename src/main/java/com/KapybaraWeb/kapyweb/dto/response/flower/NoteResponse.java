package com.KapybaraWeb.kapyweb.dto.response.flower;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.KapybaraWeb.kapyweb.entity.Category;
import com.KapybaraWeb.kapyweb.entity.Note;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteResponse {
    Long noteId;
    String noteName;
    String noteText;
    Long noteQuantity;
    String noteImage;
    Boolean noteStatus;
    Double notePrice;
    Long quantityInStock;
    List<String> categoryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate updatedAt;

    String updatedBy;

    public static NoteResponse from(Note note) {
        return NoteResponse.builder()
                .noteId(note.getNoteId())
                .noteName(note.getNoteName())
                .noteText(note.getNoteText())
                .noteQuantity(note.getNoteQuantity())
                .noteImage(note.getNoteImage())
                .noteStatus(note.getNoteStatus())
                .notePrice(note.getNotePrice())
                .quantityInStock(note.getQuantityInStock().getQuantityInStock())
                .categoryName(
                        note.getCategories().stream().map(Category::getName).collect(Collectors.toList()))
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .updatedBy(note.getUpdatedBy())
                .build();
    }
}
