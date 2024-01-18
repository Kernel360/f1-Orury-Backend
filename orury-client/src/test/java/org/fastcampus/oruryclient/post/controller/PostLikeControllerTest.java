package org.fastcampus.oruryclient.post.controller;

import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.oruryclient.post.converter.message.PostMessage;
import org.fastcampus.orurycommon.error.code.PostErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.post.db.model.PostLikePK;
import org.fastcampus.orurydomain.post.dto.PostLikeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Controller] 게시글 좋아요 관련 테스트")
class PostLikeControllerTest extends ControllerTest {

    @DisplayName("[POST] 게시글 id를 가지고 게시글 좋아요를 생성한다. - 성공")
    @WithMockUser
    @Test
    void givenPostId_When_CreatePostLike_Then_Successfully() throws Exception {
        //given
        PostLikeDto postLikeDto = createPostLikeDto();
        PostMessage code = PostMessage.POST_LIKE_CREATED;

        //when & then
        mvc.perform(post("/post/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(postLikeService).should(times(1)).createPostLike(postLikeDto);
    }

    @DisplayName("[POST] 존재하지 않는 게시글 id를 가지고 게시글 좋아요를 생성시 예외 처리 - 실패")
    @WithMockUser
    @Test
    void given_InvalidPostId_When_CreatePostLike_Then_NotFoundException() throws Exception {
        //given
        PostErrorCode code = PostErrorCode.NOT_FOUND;
        willThrow(new BusinessException(code))
                .given(postLikeService).createPostLike(any());

        //when & then
        mvc.perform(post("/post/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;
    }

    @DisplayName("[DELETE] 게시글 id를 가지고 게시글 좋아요를 삭제 - 성공")
    @WithMockUser
    @Test
    void given_PostId_When_DeletePostLike_Then_Successfully() throws Exception {
        //given
        PostLikeDto postLikeDto = createPostLikeDto();
        PostMessage code = PostMessage.POST_LIKE_DELETED;

        //when & then
        mvc.perform(delete("/post/like/" + 1L)
                .with(csrf())
                .contentType(APPLICATION_JSON_VALUE)
        );

        then(postLikeService).should(times(1)).deletePostLike(postLikeDto);
    }

    @DisplayName("[DELETE] 존재하지 않는 게시글 id를 가지고 게시글 좋아요를 삭제시 예외 처리 - 실패")
    @WithMockUser
    @Test
    void given_InvalidPostId_When_DeletePostLike_Then_NotFoundException() throws Exception {
        //given
        PostErrorCode code = PostErrorCode.NOT_FOUND;
        willThrow(new BusinessException(code))
                .given(postLikeService).deletePostLike(any());

        //when & then
        mvc.perform(delete("/post/like/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;
    }

    private PostLikePK createPostLikePK() {
        return PostLikePK.of(1L, 1L);
    }

    private PostLikeDto createPostLikeDto() {
        return PostLikeDto.of(createPostLikePK());
    }
}