package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.flower.RibbonCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.RibbonUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.RibbonResponse;
import com.KapybaraWeb.kapyweb.service.flower.RibbonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ribbon")
@RequiredArgsConstructor
public class RibbonController {
    private final RibbonService ribbonService;

    @PostMapping("/create")
    public ApiResponse<RibbonResponse> createRibbon(
            @RequestPart("data") @Valid RibbonCreateRequest request, @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<RibbonResponse>builder()
                .success(true)
                .message("Ribbon created successfully")
                .result(ribbonService.createRibbon(request, imgUrl))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RibbonResponse> getRibbonById(@PathVariable Long id) {
        return ApiResponse.<RibbonResponse>builder()
                .success(true)
                .message("Ribbon retrieved successfully")
                .result(ribbonService.getRibbonById(id))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<RibbonResponse>> getAllRibbons() {
        return ApiResponse.<List<RibbonResponse>>builder()
                .success(true)
                .message("All ribbons retrieved successfully")
                .result(ribbonService.getAllRibbons())
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<RibbonResponse> updateRibbon(
            @PathVariable Long id,
            @RequestPart("data") @Valid RibbonUpdateRequest request,
            @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<RibbonResponse>builder()
                .success(true)
                .message("Ribbon updated successfully")
                .result(ribbonService.updateRibbon(id, request, imgUrl))
                .build();
    }

    @PostMapping("/deActive/{id}")
    public ApiResponse<String> deActiveRibbon(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(ribbonService.deActiveRibbon(id))
                .build();
    }

    @PostMapping("/active/{id}")
    public ApiResponse<String> activeRibbon(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(ribbonService.activeRibbon(id))
                .build();
    }
}
