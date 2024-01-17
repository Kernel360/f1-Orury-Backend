package org.fastcampus.oruryclient.comment.controller;

import org.fastcampus.oruryclient.comment.converter.message.CommentMessage;
import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.orurycommon.error.code.CommentErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.comment.dto.CommentLikeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Controller] 댓글 좋아요 관련 테스트")
class CommentLikeControllerTest extends ControllerTest {

    @DisplayName("[POST] 유저 id, 댓글 id를 가지고 댓글 좋아요를 생성한다. - 성공")
    @WithMockUser
    @Test
    void given_UserIdAndCommentId_When_CreateCommentLike_Then_Successfully() throws Exception {
        //given
        CommentMessage code = CommentMessage.COMMENT_LIKE_CREATED;

        //when & then
        mvc.perform(post("/comment/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(commentService).should(times(1)).isValidate(any(CommentLikeDto.class));
        then(commentLikeService).should(times(1)).createCommentLike(any(CommentLikeDto.class));
    }

    @DisplayName("[POST] 유저 id, 댓글 id 중에 올바르지 않은 값을 가지고 댓글 좋아요를 생성하는 경우 예외 처리 - 실패")
    @WithMockUser
    @Test
    void given_UserIdAndCommentIdIsInvalidValue_When_CreateCommentLike_Then_NotFoundException() throws Exception {
        //given
        CommentErrorCode code = CommentErrorCode.NOT_FOUND;

        willThrow(new BusinessException(code)).given(commentService).isValidate(any(CommentLikeDto.class));

        //when & then
        mvc.perform(post("/comment/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(commentService).should(times(1)).isValidate(any(CommentLikeDto.class));
        then(commentLikeService).should(times(0)).createCommentLike(any(CommentLikeDto.class));
    }

    @DisplayName("[POST] 유저 id, 삭제된 댓글 id 가지고 댓글 좋아요를 생성하는 경우 예외 처리 - 실패")
    @WithMockUser
    @Test
    void given_UserIdAndDeletedCommentId_When_CreateCommentLike_Then_ForbiddenException() throws Exception {
        //given
        CommentErrorCode code = CommentErrorCode.FORBIDDEN;

        willThrow(new BusinessException(code)).given(commentService).isValidate(any(CommentLikeDto.class));

        //when & then
        mvc.perform(post("/comment/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(commentService).should(times(1)).isValidate(any(CommentLikeDto.class));
        then(commentLikeService).should(times(0)).createCommentLike(any(CommentLikeDto.class));
    }

    @DisplayName("[DELETE] 유저 id, 댓글 id를 가지고 댓글 좋아요를 삭제한다. - 성공")
    @WithMockUser
    @Test
    void given_UserIdAndCommentId_When_DeleteCommentLike_Then_Successfully() throws Exception {
        //given
        CommentMessage code = CommentMessage.COMMENT_LIKE_DELETED;

        //when & then
        mvc.perform(delete("/comment/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(commentService).should(times(1)).isValidate(any(CommentLikeDto.class));
        then(commentLikeService).should(times(1)).deleteCommentLike(any(CommentLikeDto.class));
    }

    @DisplayName("[DELETE] 유저 id, 댓글 id 중에 올바르지 않은 값을 가지고 댓글 좋아요 삭제시 예외 발생 - 실패")
    @WithMockUser
    @Test
    void given_UserIdAndCommentIdIsInvalidValue_When_DeleteCommentLike_Then_NotFoundException() throws Exception {
        //given
        CommentErrorCode code = CommentErrorCode.NOT_FOUND;

        willThrow(new BusinessException(code)).given(commentService).isValidate(any(CommentLikeDto.class));

        //when & then
        mvc.perform(delete("/comment/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(commentService).should(times(1)).isValidate(any(CommentLikeDto.class));
        then(commentLikeService).should(times(0)).deleteCommentLike(any(CommentLikeDto.class));
    }

    @DisplayName("[DELETE] 유저 id, 삭제된 댓글 id 가지고 댓글 좋아요를 삭제하는 경우 예외 처리 - 실패")
    @WithMockUser
    @Test
    void given_UserIdAndDeletedCommentId_When_DeleteCommentLike_Then_ForbiddenException() throws Exception {
        //given
        CommentErrorCode code = CommentErrorCode.FORBIDDEN;

        willThrow(new BusinessException(code)).given(commentService).isValidate(any(CommentLikeDto.class));

        //when & then
        mvc.perform(delete("/comment/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(commentService).should(times(1)).isValidate(any(CommentLikeDto.class));
        then(commentLikeService).should(times(0)).deleteCommentLike(any(CommentLikeDto.class));
    }
}
