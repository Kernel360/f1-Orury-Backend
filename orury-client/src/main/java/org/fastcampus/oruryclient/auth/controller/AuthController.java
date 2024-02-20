package org.fastcampus.oruryclient.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.auth.converter.message.AuthMessage;
import org.fastcampus.oruryclient.auth.converter.request.LoginRequest;
import org.fastcampus.oruryclient.auth.converter.request.RefreshTokenRequest;
import org.fastcampus.oruryclient.auth.converter.request.SignUpRequest;
import org.fastcampus.oruryclient.auth.converter.response.LoginResponse;
import org.fastcampus.oruryclient.auth.converter.response.SignUpResponse;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.oruryclient.auth.service.AuthService;
import org.fastcampus.oruryclient.auth.strategy.LoginStrategy;
import org.fastcampus.oruryclient.auth.strategy.LoginStrategyManager;
import org.fastcampus.orurycommon.error.code.AuthErrorCode;
import org.fastcampus.orurydomain.auth.dto.JwtToken;
import org.fastcampus.orurydomain.auth.dto.LoginDto;
import org.fastcampus.orurydomain.auth.dto.SignUpDto;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.user.dto.UserDto;
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
    public ApiResponse reissueJwtTokens(@RequestBody RefreshTokenRequest request) {

        JwtToken jwtToken = jwtTokenProvider.reissueJwtTokens(request.refreshToken());

        return ApiResponse.of(AuthMessage.REISSUE_ACCESS_TOKEN_SUCCESS.getMessage(), jwtToken);
    }
}
