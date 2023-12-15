package org.fastcampus.oruryapi.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestId;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestProfileImage;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestUserInfo;
import org.fastcampus.oruryapi.domain.user.converter.response.ResponseMypage;
import org.fastcampus.oruryapi.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/mypage")
    public ApiResponse<Object> readMypage(@RequestBody RequestId requestId){
        ResponseMypage responseMypage = userService.readMypage(requestId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(responseMypage)
                .build();
    }


    @PatchMapping("/mypage/profile-image")
    public ApiResponse<Object> updateProfileImage(@RequestBody RequestProfileImage requestProfileImage){
       userService.updateProfileImage(requestProfileImage);

       return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .build();
    }

    @PatchMapping("/mypage")
    public ApiResponse<Object> updateUserInfo(@RequestBody RequestUserInfo requestUserInfo){
        userService.updateUserInfo(requestUserInfo);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .build();

    }

    @DeleteMapping("/user")
    public ApiResponse<Object> deleteUser(@RequestBody RequestId requestId){
        userService.deleteUser(requestId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .build();
    }



}
