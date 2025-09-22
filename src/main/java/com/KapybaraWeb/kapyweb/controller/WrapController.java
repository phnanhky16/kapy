package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.flower.WrapCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.WrapUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.WrapResponse;
import com.KapybaraWeb.kapyweb.service.flower.WrapService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wrap")
@RequiredArgsConstructor
public class WrapController {
    private final WrapService wrapService;

    @PostMapping("/create")
    public ApiResponse<WrapResponse> createWrap(
            @RequestPart("data") @Valid WrapCreateRequest request, @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<WrapResponse>builder()
                .success(true)
                .message("Wrap created successfully")
                .result(wrapService.createWrap(request, imgUrl))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<WrapResponse> getWrapById(@PathVariable Long id) {
        return ApiResponse.<WrapResponse>builder()
                .success(true)
                .message("Wrap retrieved successfully")
                .result(wrapService.getWrapById(id))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<WrapResponse>> getAllWraps() {
        return ApiResponse.<List<WrapResponse>>builder()
                .success(true)
                .message("All wraps retrieved successfully")
                .result(wrapService.getAllWraps())
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<WrapResponse> updateWrap(
            @PathVariable Long id,
            @RequestPart("data") @Valid WrapUpdateRequest request,
            @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<WrapResponse>builder()
                .success(true)
                .message("Wrap updated successfully")
                .result(wrapService.updateWrap(id, request, imgUrl))
                .build();
    }

    @PostMapping("/deActive/{id}")
    public ApiResponse<String> deActiveWrap(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(wrapService.deActiveWrap(id))
                .build();
    }

    @PostMapping("/active/{id}")
    public ApiResponse<String> activeWrap(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(wrapService.activeWrap(id))
                .build();
    }
}
