package org.orury.client.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.auth.converter.message.AuthMessage;
import org.orury.client.auth.converter.request.LoginRequest;
import org.orury.client.auth.converter.request.SignUpRequest;
import org.orury.client.auth.converter.response.LoginResponse;
import org.orury.client.auth.converter.response.SignUpResponse;
import org.orury.client.auth.jwt.JwtTokenProvider;
import org.orury.client.auth.service.AuthService;
import org.orury.client.auth.strategy.LoginStrategy;
import org.orury.client.auth.strategy.LoginStrategyManager;
import org.orury.common.error.code.AuthErrorCode;
import org.orury.domain.auth.dto.JwtToken;
import org.orury.domain.auth.dto.LoginDto;
import org.orury.domain.auth.dto.SignUpDto;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.user.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginStrategyManager loginStrategyManager;

    @Operation(summary = "회원가입", description = "소셜 로그인을 통해 전달받은 정보를 기반으로 회원가입 수행")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        UserDto userDto = request.toDto();
        authService.signUp(userDto);

        SignUpDto signUpDto = authService.getSignUpDto(userDto);
        SignUpResponse signUpResponse = SignUpResponse.of(signUpDto);

        return ApiResponse.of(AuthMessage.SIGNUP_SUCCESS.getMessage(), signUpResponse);
    }

    @Operation(summary = "로그인", description = "소셜 로그인의 인가 코드를 받아 사용자 정보 조회 후 Access & Refresh 토큰 전달")
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        LoginStrategy strategy = loginStrategyManager.getLoginStrategy(request.signUpType());
        LoginDto loginDto = strategy.login(request);

        // 비회원인 경우 비회원 전용 토큰을 담아 return
        if (loginDto.flag()
                .equals(AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage())) {
            LoginResponse loginResponse = LoginResponse.fromNoUser(loginDto);

            return ApiResponse.of(
                    AuthErrorCode.NOT_EXISTING_USER_ACCOUNT.getStatus(),
                    AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage(),
                    loginResponse
            );
        }

        // 정상 로그인
        LoginResponse loginResponse = LoginResponse.of(loginDto);
        return ApiResponse.of(AuthMessage.LOGIN_SUCCESS.getMessage(), loginResponse);
    }

    @Operation(summary = "토큰 재발급", description = "기존 토큰을 받아, Access토큰과 Refresh토큰 모두 재발급하여 돌려준다.")
    @PostMapping("/refresh")
    public ApiResponse reissueJwtTokens(HttpServletRequest request) {

        JwtToken jwtToken = jwtTokenProvider.reissueJwtTokens(request);

        return ApiResponse.of(AuthMessage.REISSUE_ACCESS_TOKEN_SUCCESS.getMessage(), jwtToken);
    }
}
