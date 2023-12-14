package org.fastcampus.oruryapi.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.user.converter.response.UserResponse;
import org.fastcampus.oruryapi.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/mypage/{id}")
    public ApiResponse<Object> readMypage(@PathVariable Long id){
        UserResponse userResponse = userService.readMypage(id);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(userResponse)
                .build();
    }


}
