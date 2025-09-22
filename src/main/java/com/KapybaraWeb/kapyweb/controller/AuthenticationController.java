package com.KapybaraWeb.kapyweb.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.KapybaraWeb.kapyweb.dto.ApiResponse;
import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationIntrospectRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationLoginRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationLogoutRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationRefreshRequest;
import com.KapybaraWeb.kapyweb.dto.response.user.AuthenticationIntrospectResponse;
import com.KapybaraWeb.kapyweb.dto.response.user.AuthenticationLoginResponse;
import com.KapybaraWeb.kapyweb.dto.response.user.AuthenticationRefreshResponse;
import com.KapybaraWeb.kapyweb.service.user.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationLoginResponse> login(
            @RequestBody AuthenticationLoginRequest authenticationLoginRequest) {
        return ApiResponse.<AuthenticationLoginResponse>builder()
                .result(authenticationService.login(authenticationLoginRequest))
                .message("Login Successfully")
                .success(true)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<AuthenticationIntrospectResponse> introspect(
            @RequestBody AuthenticationIntrospectRequest authenticationIntrospectRequest) {
        return ApiResponse.<AuthenticationIntrospectResponse>builder()
                .result(authenticationService.introspect(authenticationIntrospectRequest))
                .success(true)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody AuthenticationLogoutRequest request)
            throws JOSEException, ParseException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .message("Successfully logged out")
                .success(true)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationRefreshResponse> refresh(@RequestBody AuthenticationRefreshRequest request)
            throws JOSEException, ParseException {
        return ApiResponse.<AuthenticationRefreshResponse>builder()
                .result(authenticationService.refreshToken(request))
                .success(true)
                .build();
    }
}
