package com.KapybaraWeb.kapyweb.service.user;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationIntrospectRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationLoginRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationLogoutRequest;
import com.KapybaraWeb.kapyweb.dto.request.user.AuthenticationRefreshRequest;
import com.KapybaraWeb.kapyweb.dto.response.user.AuthenticationIntrospectResponse;
import com.KapybaraWeb.kapyweb.dto.response.user.AuthenticationLoginResponse;
import com.KapybaraWeb.kapyweb.dto.response.user.AuthenticationRefreshResponse;
import com.KapybaraWeb.kapyweb.entity.InvalidatedToken;
import com.KapybaraWeb.kapyweb.entity.User;
import com.KapybaraWeb.kapyweb.exception.AppException;
import com.KapybaraWeb.kapyweb.exception.ErrorCode;
import com.KapybaraWeb.kapyweb.repository.InvalidatedTokenRepository;
import com.KapybaraWeb.kapyweb.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationLoginResponse login(AuthenticationLoginRequest authenticationLoginRequest) {
        log.info("SignerKey: {}", SIGNER_KEY);
        String username = authenticationLoginRequest.getUsername();
        if (userRepository.existsByUsername(username)) {
            User user = userRepository
                    .findByUsername(authenticationLoginRequest.getUsername())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (!user.getStatus()) {
                if (!user.getUpdatedBy().equals(user.getRole() + "-" + user.getFirstName())) {
                    throw new AppException(ErrorCode.LOGIN_FAILED);
                }
                user.setStatus(true);
                user.setUpdatedAt(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                user.setUpdatedBy(user.getRole() + "-" + user.getFirstName());
                userRepository.save(user);
            }
            if (!new BCryptPasswordEncoder(10).matches(authenticationLoginRequest.getPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.LOGIN_FAILED);
            }
            var token = generateToken(user);

            return AuthenticationLoginResponse.builder()
                    .token(token)
                    .role(user.getRole())
                    .success(true)
                    .build();
        }
        throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    public AuthenticationIntrospectResponse introspect(AuthenticationIntrospectRequest request) {
        boolean isValid = true;
        try {
            verifyToken(request.getToken());
        } catch (Exception e) {
            isValid = false;
        }
        return AuthenticationIntrospectResponse.builder().valid(isValid).build();
    }

    public AuthenticationRefreshResponse refreshToken(AuthenticationRefreshRequest request)
            throws ParseException, JOSEException {
        var signJwt = verifyToken(request.getToken());

        var jwtId = signJwt.getJWTClaimsSet().getJWTID();
        var expirationTime = signJwt.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtId).expiryTime(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJwt.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATION));

        var token = generateToken(user);

        return AuthenticationRefreshResponse.builder()
                .token(token)
                .role(user.getRole())
                .success(true)
                .build();
    }

    public void logout(AuthenticationLogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jwtId = signToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtId).expiryTime(expirationTime).build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("Kapibala")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATION);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATION);
        return signedJWT;
    }
}
