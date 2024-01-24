package org.fastcampus.oruryclient.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.auth.converter.message.AuthMessage;
import org.fastcampus.oruryclient.auth.converter.request.SignUpRequest;
import org.fastcampus.oruryclient.auth.converter.response.LoginResponse;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.oruryclient.auth.service.AuthService;
import org.fastcampus.orurydomain.auth.dto.JwtToken;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
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

    @Operation(summary = "로그인", description = "소셜 로그인의 인가 코드를 받아 사용자 정보 조회 후 Access & Refresh 토큰 전달")
    @GetMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestParam String code) {
        String kakaoToken = authService.getKakaoAccessToken(code).accessToken();
        UserDto userDto = authService.getUserInfo(kakaoToken);

        // Access token과 Refresh token을 모두 생성하는 메서드 호출
        JwtToken jwtToken = jwtTokenProvider.issueJwtTokens(userDto.id(), userDto.email());
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
