package org.orury.client.auth.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.auth.application.AuthFacade;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.client.auth.interfaces.request.SignUpRequest;
import org.orury.common.error.code.AuthErrorCode;
import org.orury.domain.base.converter.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.orury.client.auth.interfaces.message.AuthMessage.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthFacade authFacade;

    @Operation(summary = "회원가입", description = "소셜 로그인을 통해 전달받은 정보를 기반으로 회원가입 수행")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        var signUpResponse = authFacade.signUp(request.toDto());
        return ApiResponse.of(SIGNUP_SUCCESS.getMessage(), signUpResponse);
    }

    @Operation(summary = "로그인", description = "소셜 로그인의 인가 코드를 받아 사용자 정보 조회 후 Access & Refresh 토큰 전달")
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        var loginResponse = authFacade.login(request);
        if (loginResponse.isNoUser()) {
            return ApiResponse.of(AuthErrorCode.NOT_EXISTING_USER_ACCOUNT.getStatus(), NOT_EXISTING_USER_ACCOUNT.getMessage(), loginResponse);
        } else {
            return ApiResponse.of(LOGIN_SUCCESS.getMessage(), loginResponse);
        }
    }

    @Operation(summary = "토큰 재발급", description = "기존 토큰을 받아, Access토큰과 Refresh토큰 모두 재발급하여 돌려준다.")
    @PostMapping("/refresh")
    public ApiResponse reissueJwtTokens(HttpServletRequest request) {
        var jwtToken = authFacade.reissueJwtTokens(request);
        return ApiResponse.of(REISSUE_ACCESS_TOKEN_SUCCESS.getMessage(), jwtToken);
    }
}
