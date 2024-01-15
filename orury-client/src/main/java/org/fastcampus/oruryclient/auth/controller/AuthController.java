package org.fastcampus.oruryclient.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.auth.converter.request.LoginRequest;
import org.fastcampus.oruryclient.auth.converter.request.SignUpRequest;
import org.fastcampus.oruryclient.auth.converter.response.LoginResponse;
import org.fastcampus.oruryclient.auth.jwt.JwtToken;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.oruryclient.auth.service.AuthService;
import org.fastcampus.oruryclient.auth.util.AuthMessage;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "회원가입", description = "소셜 로그인을 통해 전달받은 정보를 기반으로 회원가입 수행")
    @PostMapping("/sign-up")
    public ApiResponse<Object> signUp(@RequestBody SignUpRequest request) {
        UserDto userDto = request.toDto();

        authService.signUp(userDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(AuthMessage.SIGNUP_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "로그인", description = "유저 정보를 받아 로그인 후, 로그인 성공 여부를 돌려준다.")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        UserDto userDto = authService.getUserDtoByEmail(request.email());
        JwtToken jwtToken = jwtTokenProvider.createJwtToken(userDto);

        LoginResponse loginResponse = LoginResponse.of(userDto, jwtToken);

        return ApiResponse.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message(AuthMessage.LOGIN_SUCCESS.getMessage())
                .data(loginResponse)
                .build();
    }

    @Operation(summary = "토큰 재발급", description = "기존 토큰을 받아, Access토큰과 Refresh토큰 모두 재발급하여 돌려준다.")
    @PostMapping("/refresh")
    public ApiResponse<JwtToken> reissueJwtTokens(@RequestBody JwtToken request) {

        JwtToken jwtToken = jwtTokenProvider.reissueJwtTokens(request.refreshToken());

        return ApiResponse.<JwtToken>builder()
                .status(HttpStatus.OK.value())
                .message(AuthMessage.REISSUE_ACCESS_TOKEN_SUCCESS.getMessage())
                .data(jwtToken)
                .build();
    }
}
