package org.orury.client.user.application;


import lombok.RequiredArgsConstructor;
import org.orury.client.comment.application.CommentService;
import org.orury.client.global.IdIdentifiable;
import org.orury.client.global.WithCursorResponse;
import org.orury.client.post.application.PostService;
import org.orury.client.review.application.ReviewService;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.client.user.interfaces.response.MyCommentResponse;
import org.orury.client.user.interfaces.response.MyPostResponse;
import org.orury.client.user.interfaces.response.MyReviewResponse;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Function;

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

    public WithCursorResponse<MyPostResponse> getPostsByUserId(Long id, Long cursor) {
        List<PostDto> postDtos = postService.getPostDtosByUserId(id, cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));

        return convertDtosToWithCursorResponse(postDtos, MyPostResponse::of, cursor);
    }

    public WithCursorResponse<MyReviewResponse> getReviewsByUserId(Long id, Long cursor) {
        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByUserId(id, cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));

        return convertDtosToWithCursorResponse(reviewDtos, MyReviewResponse::of, cursor);
    }

    public WithCursorResponse<MyCommentResponse> getCommentsByUserId(Long id, Long cursor) {
        List<CommentDto> commmentDtos = commentService.getCommentDtosByUserId(id, cursor);

        return convertDtosToWithCursorResponse(commmentDtos, MyCommentResponse::of, cursor);
    }

    public void deleteUser(Long id) {
        UserDto userDto = userService.getUserDtoById(id);
        userService.deleteUser(userDto);
    }

    private <T, R extends IdIdentifiable> WithCursorResponse<R> convertDtosToWithCursorResponse(List<T> dtos, Function<T, R> toResponseFunction, Long cursor) {
        List<R> responses = dtos.stream()
                .map(toResponseFunction)
                .toList();

        return WithCursorResponse.of(responses, cursor);
    }


}
