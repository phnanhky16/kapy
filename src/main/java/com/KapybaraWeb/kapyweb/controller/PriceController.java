package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.flower.PriceRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.PriceResponse;
import com.KapybaraWeb.kapyweb.service.flower.PriceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/price")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @PostMapping("/create")
    public ApiResponse<PriceResponse> createPrice(@RequestBody @Valid PriceRequest priceRequest) {
        return ApiResponse.<PriceResponse>builder()
                .result(priceService.createPrice(priceRequest))
                .message("Create price successfully")
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PriceResponse> getPriceById(@PathVariable Long id) {
        return ApiResponse.<PriceResponse>builder()
                .result(priceService.getPriceById(id))
                .message("Get price successfully")
                .success(true)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<PriceResponse>> getAllPrices() {
        return ApiResponse.<List<PriceResponse>>builder()
                .result(priceService.getAllPrice())
                .message("Get all prices successfully")
                .success(true)
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<PriceResponse> updatePrice(
            @PathVariable Long id, @Valid @RequestBody PriceRequest priceRequest) {
        return ApiResponse.<PriceResponse>builder()
                .result(priceService.updatePrice(id, priceRequest))
                .message("Update price successfully")
                .success(true)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deletePrice(@PathVariable Long id) {
        priceService.deletePrice(id);
        return ApiResponse.<String>builder()
                .result("Price deleted successfully")
                .message("Price with id " + id + " has been deleted")
                .success(true)
                .build();
    }
}
