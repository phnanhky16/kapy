package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.flower.NoteCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.NoteUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.NoteResponse;
import com.KapybaraWeb.kapyweb.service.flower.NoteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping("/create")
    public ApiResponse<NoteResponse> createNote(
            @RequestPart("data") @Valid NoteCreateRequest request, @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<NoteResponse>builder()
                .success(true)
                .message("Note created successfully")
                .result(noteService.createNote(request, imgUrl))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<NoteResponse> getNoteById(@PathVariable Long id) {
        return ApiResponse.<NoteResponse>builder()
                .success(true)
                .message("Note retrieved successfully")
                .result(noteService.getNoteById(id))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<NoteResponse>> getAllNotes() {
        return ApiResponse.<List<NoteResponse>>builder()
                .success(true)
                .message("All notes retrieved successfully")
                .result(noteService.getAllNotes())
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<NoteResponse> updateNote(
            @PathVariable Long id,
            @RequestPart("data") @Valid NoteUpdateRequest request,
            @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<NoteResponse>builder()
                .success(true)
                .message("Note updated successfully")
                .result(noteService.updateNote(id, request, imgUrl))
                .build();
    }

    @PostMapping("/deActive/{id}")
    public ApiResponse<String> deActiveNote(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(noteService.deActiveNote(id))
                .build();
    }

    @PostMapping("/active/{id}")
    public ApiResponse<String> activeNote(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(noteService.activeNote(id))
                .build();
    }
}
