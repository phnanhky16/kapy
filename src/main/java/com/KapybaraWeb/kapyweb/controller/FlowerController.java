package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.flower.FlowerCreateRequest;
import com.KapybaraWeb.kapyweb.dto.request.flower.FlowerUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.FlowerResponse;
import com.KapybaraWeb.kapyweb.service.flower.FlowerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/flower")
@RequiredArgsConstructor
public class FlowerController {
    private final FlowerService flowerService;

    @PostMapping("/create")
    public ApiResponse<FlowerResponse> createFlower(
            @RequestPart("data") @Valid FlowerCreateRequest request, @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<FlowerResponse>builder()
                .success(true)
                .message("Flower created successfully")
                .result(flowerService.createFlower(request, imgUrl))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<FlowerResponse> getFlowerById(@PathVariable Long id) {
        return ApiResponse.<FlowerResponse>builder()
                .success(true)
                .message("Flower retrieved successfully")
                .result(flowerService.getFlowerById(id))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<FlowerResponse>> getAllFlowers() {
        return ApiResponse.<List<FlowerResponse>>builder()
                .success(true)
                .message("All flowers retrieved successfully")
                .result(flowerService.getAllFlowers())
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<FlowerResponse> updateFlower(
            @PathVariable Long id,
            @RequestPart("data") @Valid FlowerUpdateRequest request,
            @RequestPart("imgUrl") MultipartFile imgUrl) {
        return ApiResponse.<FlowerResponse>builder()
                .success(true)
                .message("Flower updated successfully")
                .result(flowerService.updateFlower(id, request, imgUrl))
                .build();
    }

    @PostMapping("/deActive/{id}")
    public ApiResponse<String> deActiveFlower(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(flowerService.deActiveFlower(id))
                .build();
    }

    @PostMapping("/active/{id}")
    public ApiResponse<String> activeFlower(@PathVariable Long id) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(flowerService.activeFlower(id))
                .build();
    }
}
