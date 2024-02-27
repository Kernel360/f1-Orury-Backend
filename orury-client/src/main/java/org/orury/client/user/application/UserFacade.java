package org.orury.client.user.application;

import org.orury.client.comment.service.CommentService;
import org.orury.client.post.service.PostService;
import org.orury.client.review.service.ReviewService;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    public UserDto readMypage(Long id) {
        return userService.getUserDtoById(id);
    }

    public void updateProfileImage(Long id, MultipartFile image) {
        UserDto userDto = userService.getUserDtoById(id);
        userService.updateProfileImage(userDto, image);
    }

    public void updateUserInfo(Long id, UserInfoRequest userInfoRequest) {
        UserDto userDto = userService.getUserDtoById(id);
        UserDto updateUserDto = UserInfoRequest.toDto(userDto, userInfoRequest);
        userService.updateUserInfo(updateUserDto);
    }

    public void deleteUser(Long id) {
        UserDto userDto = userService.getUserDtoById(id);
        userService.deleteUser(userDto);
    }
}
