//package org.orury.client.comment.controller;
//
//import org.orury.client.comment.converter.message.CommentMessage;
//import org.orury.client.config.ControllerTest;
//import org.orury.client.config.WithUserPrincipal;
//import org.orury.common.error.code.CommentErrorCode;
//import org.orury.common.error.exception.BusinessException;
//import org.orury.domain.comment.dto.CommentLikeDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.*;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@DisplayName("[Controller] 댓글 좋아요 관련 테스트")
//@WithUserPrincipal
//class CommentLikeControllerTest extends ControllerTest {
//
//    @DisplayName("[POST] 유저 id, 댓글 id를 가지고 댓글 좋아요를 생성한다. - 성공")
//    @Test
//    void given_UserIdAndCommentId_When_CreateCommentLike_Then_Successfully() throws Exception {
//        //given
//        CommentMessage code = CommentMessage.COMMENT_LIKE_CREATED;
//
//        //when & then
//        mvc.perform(post("/api/v1/comments/like/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//        ;
//
//        then(commentService).should(times(1))
//                .isValidate(any(CommentLikeDto.class));
//        then(commentLikeService).should(times(1))
//                .createCommentLike(any(CommentLikeDto.class));
//    }
//
//    @DisplayName("[POST] 유저 id, 댓글 id 중에 올바르지 않은 값을 가지고 댓글 좋아요를 생성하는 경우 예외 처리 - 실패")
//    @Test
//    void given_UserIdAndCommentIdIsInvalidValue_When_CreateCommentLike_Then_NotFoundException() throws Exception {
//        //given
//        CommentErrorCode code = CommentErrorCode.NOT_FOUND;
//
//        willThrow(new BusinessException(code)).given(commentService)
//                .isValidate(any(CommentLikeDto.class));
//
//        //when & then
//        mvc.perform(post("/api/v1/comments/like/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(commentService).should(times(1))
//                .isValidate(any(CommentLikeDto.class));
//        then(commentLikeService).should(times(0))
//                .createCommentLike(any(CommentLikeDto.class));
//    }
//
//    @DisplayName("[POST] 유저 id, 삭제된 댓글 id 가지고 댓글 좋아요를 생성하는 경우 예외 처리 - 실패")
//    @Test
//    void given_UserIdAndDeletedCommentId_When_CreateCommentLike_Then_ForbiddenException() throws Exception {
//        //given
//        CommentErrorCode code = CommentErrorCode.FORBIDDEN;
//
//        willThrow(new BusinessException(code)).given(commentService)
//                .isValidate(any(CommentLikeDto.class));
//
//        //when & then
//        mvc.perform(post("/api/v1/comments/like/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(commentService).should(times(1))
//                .isValidate(any(CommentLikeDto.class));
//        then(commentLikeService).should(times(0))
//                .createCommentLike(any(CommentLikeDto.class));
//    }
//
//    @DisplayName("[DELETE] 유저 id, 댓글 id를 가지고 댓글 좋아요를 삭제한다. - 성공")
//    @Test
//    void given_UserIdAndCommentId_When_DeleteCommentLike_Then_Successfully() throws Exception {
//        //given
//        CommentMessage code = CommentMessage.COMMENT_LIKE_DELETED;
//
//        //when & then
//        mvc.perform(delete("/api/v1/comments/like/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//        ;
//
//        then(commentService).should(times(1))
//                .isValidate(any(CommentLikeDto.class));
//        then(commentLikeService).should(times(1))
//                .deleteCommentLike(any(CommentLikeDto.class));
//    }
//
//    @DisplayName("[DELETE] 유저 id, 댓글 id 중에 올바르지 않은 값을 가지고 댓글 좋아요 삭제시 예외 발생 - 실패")
//    @Test
//    void given_UserIdAndCommentIdIsInvalidValue_When_DeleteCommentLike_Then_NotFoundException() throws Exception {
//        //given
//        CommentErrorCode code = CommentErrorCode.NOT_FOUND;
//
//        willThrow(new BusinessException(code)).given(commentService)
//                .isValidate(any(CommentLikeDto.class));
//
//        //when & then
//        mvc.perform(delete("/api/v1/comments/like/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(commentService).should(times(1))
//                .isValidate(any(CommentLikeDto.class));
//        then(commentLikeService).should(times(0))
//                .deleteCommentLike(any(CommentLikeDto.class));
//    }
//
//    @DisplayName("[DELETE] 유저 id, 삭제된 댓글 id 가지고 댓글 좋아요를 삭제하는 경우 예외 처리 - 실패")
//    @Test
//    void given_UserIdAndDeletedCommentId_When_DeleteCommentLike_Then_ForbiddenException() throws Exception {
//        //given
//        CommentErrorCode code = CommentErrorCode.FORBIDDEN;
//
//        willThrow(new BusinessException(code)).given(commentService)
//                .isValidate(any(CommentLikeDto.class));
//
//        //when & then
//        mvc.perform(delete("/api/v1/comments/like/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(commentService).should(times(1))
//                .isValidate(any(CommentLikeDto.class));
//        then(commentLikeService).should(times(0))
//                .deleteCommentLike(any(CommentLikeDto.class));
//    }
//}
