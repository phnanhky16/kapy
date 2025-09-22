package com.KapybaraWeb.kapyweb.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.user.UserChangePasswordRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.UserRegisterRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.UserResetPasswordRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.UserUpdateRequest;
import com.KapybaraWeb.kapyweb.dto.response.user.UserResponse;
import com.KapybaraWeb.kapyweb.service.config.MailService;
import com.KapybaraWeb.kapyweb.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/register_user")
    ApiResponse<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest user) {
        String username = user.getUsername();
        return ApiResponse.<UserResponse>builder()
                .result(userService.registerUser(user))
                .success(true)
                .message(username + " registered successfully")
                .build();
    }

    @PostMapping("/register_staff")
    ApiResponse<UserResponse> registerStaff(@Valid @RequestBody UserRegisterRequest user) {
        String username = user.getUsername();
        return ApiResponse.<UserResponse>builder()
                .result(userService.registerStaff(user))
                .success(true)
                .message(username + " registered successfully")
                .build();
    }

    @PostMapping("/register_order")
    ApiResponse<UserResponse> registerOrder(@Valid @RequestBody UserRegisterRequest user) {
        String username = user.getUsername();
        return ApiResponse.<UserResponse>builder()
                .result(userService.registerOrder(user))
                .success(true)
                .message(username + " registered successfully")
                .build();
    }

    @PostMapping("/register_hr")
    ApiResponse<UserResponse> registerHr(@Valid @RequestBody UserRegisterRequest user) {
        String username = user.getUsername();
        return ApiResponse.<UserResponse>builder()
                .result(userService.registerHr(user))
                .success(true)
                .message(username + " registered successfully")
                .build();
    }

    @PostMapping("/register_florist")
    ApiResponse<UserResponse> registerFlorist(@Valid @RequestBody UserRegisterRequest user) {
        String username = user.getUsername();
        return ApiResponse.<UserResponse>builder()
                .result(userService.registerFlorist(user))
                .success(true)
                .message(username + " registered successfully")
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .success(true)
                .message("My information successfully")
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .success(true)
                .message("User information by Id successfully")
                .build();
    }

    @GetMapping("/all_users")
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .success(true)
                .message("All user information successfully")
                .build();
    }

    @GetMapping("/all_staffs")
    ApiResponse<List<UserResponse>> getAllStaffs() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllStaffs())
                .success(true)
                .message("All staff information successfully")
                .build();
    }

    @GetMapping("/all_orders")
    ApiResponse<List<UserResponse>> getAllOrders() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllOrders())
                .message("All order information successfully")
                .success(true)
                .build();
    }

    @GetMapping("/all_florists")
    ApiResponse<List<UserResponse>> getAllFlorists() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllFlorists())
                .message("All florist information successfully")
                .success(true)
                .build();
    }

    @GetMapping("/all_hrs")
    ApiResponse<List<UserResponse>> getAllHrs() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllHrs())
                .message("All HR information successfully")
                .success(true)
                .build();
    }

    @PutMapping("/update_user")
    ApiResponse<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest user) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(user))
                .message("update user successfully")
                .success(true)
                .build();
    }

    @PutMapping("update_user/{userId}")
    ApiResponse<UserResponse> updateUserById(@PathVariable Long userId, @Valid @RequestBody UserUpdateRequest user) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUserById(userId, user))
                .message("update user successfully")
                .success(true)
                .build();
    }

    @PostMapping("/deActive_user/{userId}")
    ApiResponse<String> deActiveUser(@PathVariable Long userId) {
        return ApiResponse.<String>builder()
                .result(userService.DeactiveUser(userId))
                .success(true)
                .build();
    }

    @PostMapping("/deActive_user_myself")
    ApiResponse<String> deActiveUserMyself() {
        return ApiResponse.<String>builder()
                .result(userService.DeactiveUserMyself())
                .success(true)
                .build();
    }

    @PostMapping("/active_user/{userId}")
    ApiResponse<String> activeUser(@PathVariable Long userId) {
        return ApiResponse.<String>builder()
                .result(userService.ActiveUser(userId))
                .success(true)
                .build();
    }

    @PostMapping("/active_user_myself")
    ApiResponse<String> activeUserMyself() {
        return ApiResponse.<String>builder()
                .result(userService.ActiveUserMyself())
                .success(true)
                .build();
    }

    @PostMapping("/change_password")
    ApiResponse<String> changePassword(@Valid @RequestBody UserChangePasswordRequest user) {
        return ApiResponse.<String>builder()
                .result(userService.changePassword(user))
                .success(true)
                .build();
    }

    @PostMapping("/reset_password/{userId}")
    ApiResponse<String> resetPassword(@PathVariable Long userId, @Valid @RequestBody UserResetPasswordRequest user) {
        return ApiResponse.<String>builder()
                .result(userService.resetPassword(userId, user))
                .success(true)
                .build();
    }
}
