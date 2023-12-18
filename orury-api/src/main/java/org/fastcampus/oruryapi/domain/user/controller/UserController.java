package org.fastcampus.oruryapi.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.converter.ApiResponse;
import org.fastcampus.oruryapi.domain.user.converter.request.IdRequest;
import org.fastcampus.oruryapi.domain.user.converter.request.ProfileImageRequest;
import org.fastcampus.oruryapi.domain.user.converter.request.UserInfoRequest;
import org.fastcampus.oruryapi.domain.user.converter.response.MypageResponse;
import org.fastcampus.oruryapi.domain.user.service.UserService;
import org.fastcampus.oruryapi.domain.user.util.UserMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "마이페이지 조회", description = "id에 해당하는 유저의 정보를 조회합니다. 닉네임, 생일, 프로필사진, 이메일, 성별이 return 됩니다. ")
    @GetMapping("/mypage")
    public ApiResponse<Object> readMypage(@PathVariable Long id){
        MypageResponse mypageResponse = userService.readMypage(id);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_READ.getMessage())
                .data(mypageResponse)
                .build();
    }


    @Operation(summary = "프로필 사진 수정", description = "request에 담긴 id에 해당하는 유저의 프로필 사진을 수정합니다.")
    @PatchMapping("/mypage/profile-image")
    public ApiResponse<Object> updateProfileImage(@RequestBody ProfileImageRequest profileImageRequest){
       userService.updateProfileImage(profileImageRequest);

       return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_PROFILEIMAGE_UPDATED.getMessage())
                .build();
    }

    @Operation(summary = "유저 정보 수정", description = "request에 담긴 id에 해당하는 유저의 정보를 수정합니다. 현재 닉네임만 수정 가능합니다. ")
    @PatchMapping("/mypage")
    public ApiResponse<Object> updateUserInfo(@RequestBody UserInfoRequest userInfoRequest){
        userService.updateUserInfo(userInfoRequest);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_UPDATED.getMessage())
                .build();

    }

    @Operation(summary = "회원 탈퇴", description = "id에 해당하는 회원을 탈퇴합니다. ")
    @DeleteMapping("/user")
    public ApiResponse<Object> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(UserMessage.USER_DELETED.getMessage())
                .build();
    }

}
