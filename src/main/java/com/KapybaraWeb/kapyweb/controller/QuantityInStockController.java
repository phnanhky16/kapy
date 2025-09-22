package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.flower.QuantityInStockRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.QuantityInStockResponse;
import com.KapybaraWeb.kapyweb.service.flower.QuantityInStockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/quantity")
@RequiredArgsConstructor
public class QuantityInStockController {
    private final QuantityInStockService quantityInStockService;

    @PostMapping("/create")
    public ApiResponse<QuantityInStockResponse> createQuantityInStock(
            @Valid @RequestBody QuantityInStockRequest quantityInStockRequest) {
        return ApiResponse.<QuantityInStockResponse>builder()
                .result(quantityInStockService.createQuantityInStock(quantityInStockRequest))
                .success(true)
                .message("create quantityInStock successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<QuantityInStockResponse> getQuantityInStockById(@PathVariable Long id) {
        return ApiResponse.<QuantityInStockResponse>builder()
                .result(quantityInStockService.getQuantityInStockById(id))
                .success(true)
                .message("get quantityInStock successfully")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<QuantityInStockResponse>> getAllQuantityInStock() {
        return ApiResponse.<List<QuantityInStockResponse>>builder()
                .result(quantityInStockService.getAllQuantityInStock())
                .success(true)
                .message("get all quantityInStock successfully")
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<QuantityInStockResponse> updateQuantityInStock(
            @PathVariable Long id, @Valid @RequestBody QuantityInStockRequest quantityInStockRequest) {
        return ApiResponse.<QuantityInStockResponse>builder()
                .result(quantityInStockService.updateQuantityInStock(id, quantityInStockRequest))
                .success(true)
                .message("update quantityInStock successfully")
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteQuantityInStock(@PathVariable Long id) {
        quantityInStockService.deleteQuantityInStock(id);
        return ApiResponse.<String>builder()
                .result("Quantity in stock with id " + id + " has been deleted")
                .success(true)
                .message("delete quantityInStock successfully")
                .build();
    }
}
