package com.KapybaraWeb.kapyweb.controller;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.shop.AboutRequest;
import com.KapybaraWeb.kapyweb.dto.response.shop.AboutResponse;
import com.KapybaraWeb.kapyweb.service.shop.AboutService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/about")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AboutController {
    AboutService aboutService;
    @PostMapping("/create")
    public ApiResponse<AboutResponse> createAbout(
            @RequestPart("data") @Valid AboutRequest request, @RequestPart("imgUrl") MultipartFile imgUrl
    ) {
        return ApiResponse.<AboutResponse>builder()
                .success(true)
                .message("About created successfully")
                .result(aboutService.createAbout(request, imgUrl))
                .build();
    }
    @PostMapping("/update")
    public ApiResponse<AboutResponse> updateAbout(
            @RequestPart("data") @Valid AboutRequest request, @RequestPart("imgUrl") MultipartFile imgUrl
    ) {
        return ApiResponse.<AboutResponse>builder()
                .success(true)
                .message("About updated successfully")
                .result(aboutService.createAbout(request, imgUrl))
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<AboutResponse> getAboutById(@PathVariable Long id){
        return ApiResponse.<AboutResponse>builder()
                .success(true)
                .message("About retrieved successfully")
                .result(aboutService.getAboutById(id))
                .build();
    }
    @GetMapping("/all")
    public ApiResponse<List<AboutResponse>> getAllAbouts(){
        return ApiResponse.<List<AboutResponse>>builder()
                .success(true)
                .message("All abouts retrieved successfully")
                .result(aboutService.getAllAbouts())
                .build();
    }
    @PutMapping("/active/{id}")
    public ApiResponse<String> activeAbout(@PathVariable Long id){
        return ApiResponse.<String>builder()
                .success(true)
                .message("About activated successfully")
                .result("About with id " + id + " activated")
                .build();
    }
    @PatchMapping("/deactive/{id}")
    public ApiResponse<String> deactiveAbout(@PathVariable Long id){
        return ApiResponse.<String>builder()
                .success(true)
                .message("About deactivated successfully")
                .result("About with id " + id + " deactivated")
                .build();
    }

}
