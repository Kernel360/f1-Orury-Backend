package org.fastcampus.oruryapi.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.auth.converter.request.SignInRequest;
import org.fastcampus.oruryapi.domain.auth.converter.request.SignUpRequest;
import org.fastcampus.oruryapi.domain.auth.converter.response.SignInResponse;
import org.fastcampus.oruryapi.domain.auth.service.AuthService;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.global.constants.Constants;
import org.fastcampus.oruryapi.global.message.info.InfoMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "회원가입", description = "소셜 로그인을 통해 전달받은 정보를 기반으로 회원가입 수행")
    @PostMapping("/sign-up")
    public ApiResponse<Object> signUp(@RequestBody SignUpRequest request) {
        UserDto userDto = UserDto.from(request);

        authService.signUp(userDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.USER_SIGNUP_SUCCESS.getMessage())
                .build();
    }

    @Operation(summary = "로그인", description = "유저 정보를 받아 로그인 후, 로그인 성공 여부를 돌려준다.")
    @PostMapping("/sign-in")
    public ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest request) {
        UserDto userDto = authService.signIn(request);
        SignInResponse signInResponse = SignInResponse.of(userDto);

        return ApiResponse.<SignInResponse>builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.USER_LOGIN_SUCCESS.getMessage())
                .data(signInResponse)
                .build();
    }
}
