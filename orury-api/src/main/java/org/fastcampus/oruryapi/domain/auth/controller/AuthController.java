package org.fastcampus.oruryapi.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.auth.converter.request.SignInRequest;
import org.fastcampus.oruryapi.domain.auth.service.AuthService;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.global.constants.Constants;
import org.fastcampus.oruryapi.global.message.info.InfoMessage;
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

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ApiResponse<UserDto> signIn(@RequestBody SignInRequest request) {
        UserDto userDto = authService.signIn(request);

        return ApiResponse.<UserDto>builder()
                .status(HttpStatus.OK.value())
                .message(InfoMessage.USER_LOGIN_SUCCESS.toString())
                .data(userDto)
                .build();
    }
}
