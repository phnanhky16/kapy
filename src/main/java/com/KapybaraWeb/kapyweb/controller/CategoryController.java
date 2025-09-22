package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.flower.CategoryRequest;
import com.KapybaraWeb.kapyweb.dto.response.flower.CategoryResponse;
import com.KapybaraWeb.kapyweb.service.flower.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .message("Create category successfully")
                .result(categoryService.createCategory(request))
                .success(true)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ApiResponse.<CategoryResponse>builder()
                .message("Get category successfully")
                .result(categoryService.getCategoryById(id))
                .success(true)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get all categories successfully")
                .result(categoryService.getAllCategories())
                .success(true)
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .message("Update category successfully")
                .result(categoryService.updateCategory(id, request))
                .success(true)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder()
                .message("Delete category successfully")
                .result("Category with id " + id + " has been deleted")
                .success(true)
                .build();
    }
}
