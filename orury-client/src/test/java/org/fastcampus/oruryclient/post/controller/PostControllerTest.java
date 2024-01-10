package org.fastcampus.oruryclient.post.controller;

import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.post.service.PostLikeService;
import org.fastcampus.oruryclient.post.service.PostService;
import org.fastcampus.oruryclient.user.service.UserService;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private PostService postService;
    @MockBean
    private UserService userService;
    @MockBean
    private PostLikeService postLikeService;

    @DisplayName("[GET] 게시글 id로 게시글 상세 조회 - 성공")
    @WithMockUser
    @Test
    void when_PostId_Then_PostDetailSuccessfully() throws Exception {
        //given
        Long postId = 1L;
        Long userId = NumberConstants.USER_ID;
        boolean isLike = true;
        PostDto postDto = createPostDto(postId);

        given(postService.getPostDtoById(postId)).willReturn(postDto);
        given(postLikeService.isLiked(userId, postId)).willReturn(isLike);

        //when
        mvc.perform(get("/post/" + postId).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(postId))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"))
                .andExpect(jsonPath("$.data.is_like").value(true));

        //then
        then(postService).should().getPostDtoById(postId);
        then(postService).should().addViewCount(postDto);
        then(postLikeService).should().isLiked(userId, postId);
    }

    private PostDto createPostDto(Long id) {
        return PostDto.of(
                id,
                "title",
                "content",
                0,
                0,
                0,
                "",
                1,
                createUserDto(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private UserDto createUserDto() {
        return UserDto.of(
                NumberConstants.USER_ID,
                "mail@mail",
                "user",
                "password",
                1,
                1,
                null,
                "bio",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}