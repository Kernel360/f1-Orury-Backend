package org.orury.client.post.interfaces;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ControllerTest;
import org.orury.client.config.WithUserPrincipal;
import org.orury.client.post.interfaces.message.PostMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@DisplayName("[Controller] 게시글 관련 테스트")
@WithUserPrincipal
class PostControllerTest extends ControllerTest {

    @DisplayName("[POST] 게시글 정보 request와 이미지 파일들을 받아 게시글 생성 - 성공")
    @Test
    void when_PostRequestAndImages_Then_CreatePostSuccessfully() throws Exception {
        //given
        var file = mock(MockMultipartFile.class);
        var request = mock(MockMultipartFile.class);

        //when
        mvc.perform(multipart(HttpMethod.POST, "/api/v1/posts")
                        .file(request)
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(PostMessage.POST_CREATED.getMessage()));

        //then
        then(postFacade).should(times(1)).createPost(any(), any(), any());
    }


    @DisplayName("[PATCH] 게시글 정보를 받아 게시글을 수정 - 성공 (이미지 있는 경우)")
    @Test
    void when_RequestContainsImage_Then_UpdatePostSuccessfully() throws Exception {
        //given
        var file = mock(MockMultipartFile.class);
        var request = mock(MockMultipartFile.class);
        var message = PostMessage.POST_UPDATED;

        //when
        mvc.perform(multipart(PATCH, "/api/v1/posts")
                        .file(request)
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        //then
        then(postFacade).should(times(1)).updatePost(any(), any(), any());
    }

    @DisplayName("[PATCH] 게시글 정보를 받아 게시글을 수정 - 성공 (이미지 없는 경우)")
    @Test
    void when_RequestDoesNotContainsImage_Then_UpdatePostSuccessfully() throws Exception {
        //given
        var request = mock(MockMultipartFile.class);
        var message = PostMessage.POST_UPDATED;

        //when
        mvc.perform(multipart(PATCH, "/api/v1/posts")
                        .file(request)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        //then
        then(postFacade).should(times(1)).updatePost(any(), any(), any());
    }

    @DisplayName("[DELETE] 게시글 id를 받아 게시글을 삭제  - 성공")
    @Test
    void given_PostId_When_DeletePostSuccessFully() throws Exception {
        //given
        Long postId = 1L;
        var code = PostMessage.POST_DELETED;

        //when
        mvc.perform(delete("/api/v1/posts/" + postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        //then
        then(postFacade).should(times(1)).deletePost(any(), any());
    }
}
