package org.orury.client.post.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.FacadeTest;
import org.orury.client.post.interfaces.request.PostRequest;
import org.orury.client.post.interfaces.response.PostResponse;
import org.orury.client.post.interfaces.response.PostsResponse;
import org.orury.client.post.interfaces.response.PostsWithPageResponse;
import org.orury.domain.DomainFixtureFactory;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@DisplayName("[Facade] 게시글 Facade 테스트")
class PostFacadeTest extends FacadeTest {

    @DisplayName("유저 ID, PostRequest, 이미지 파일들을 받아 게시글을 생성한다.")
    @Test
    void given_UserIdAndPostRequestAndMultipartFiles_When_CreatePost_Then_Success() {
        //given
        var user = createUserDto(1L);
        var request = createPostRequest(null);
        var post = request.toDto(user);
        var files = List.of(mock(MultipartFile.class));
        given(userService.getUserDtoById(anyLong())).willReturn(user);
        willDoNothing().given(postService).createPost(post, files);

        //when
        postFacade.createPost(1L, request, files);

        //then
        then(userService).should(times(1)).getUserDtoById(anyLong());
        then(postService).should(times(1)).createPost(post, files);
    }

    @DisplayName("유저 ID, PostRequest, 이미지 파일들을 받아 게시글을 수정한다.")
    @Test
    void given_UserIdAndPostRequestAndMultipartFiles_When_UpdatePost_Then_Success() {
        //given
        var user = createUserDto(1L);
        var request = createPostRequest(1L);
        var post = request.toDto(user);
        var files = createMultipartFiles();
        given(userService.getUserDtoById(anyLong())).willReturn(user);
        given(postService.getPostDtoById(anyLong(), anyLong())).willReturn(post);
        willDoNothing().given(postService).createPost(post, files);

        //when
        postFacade.updatePost(1L, request, files);

        //then
        then(userService).should(times(1)).getUserDtoById(anyLong());
        then(postService).should(times(1)).getPostDtoById(anyLong(), anyLong());
        then(postService).should(times(1)).createPost(post, files);
    }

    @DisplayName("유저 ID, 게시글 ID를 받아 게시글을 삭제한다.")
    @Test
    void given_UserIdAndPostId_When_DeletePost_Then_Success() {
        //given
        var post = createPostDto(1L);
        given(postService.getPostDtoById(anyLong(), anyLong())).willReturn(post);
        willDoNothing().given(postService).deletePost(post);

        //when
        postFacade.deletePost(1L, 1L);

        //then
        then(postService).should(times(1)).getPostDtoById(anyLong(), anyLong());
        then(postService).should(times(1)).deletePost(post);
    }

    @DisplayName("유저 ID, 게시글 ID를 받아 게시글을 조회한다.")
    @Test
    void given_UserIdAndPostId_When_GetPostById_Then_ReturnPostResponse() {
        //given
        var user = createUserDto(1L);
        var post = createPostDto(1L);

        given(postService.getPostDtoById(anyLong())).willReturn(post);
        given(userService.getUserDtoById(anyLong())).willReturn(user);
        willDoNothing().given(postService).updateViewCount(anyLong());

        //when
        var actual = postFacade.getPostById(1L, 1L);

        //then
        assertThat(actual)
                .isNotNull()
                .isEqualTo(PostResponse.of(post, user));
        then(postService).should(times(1)).getPostDtoById(anyLong());
        then(userService).should(times(1)).getUserDtoById(anyLong());
        then(postService).should(times(1)).updateViewCount(anyLong());
    }

    @DisplayName("카테고리, 커서, 페이지 정보를 받아 게시글 목록을 조회한다.")
    @Test
    void given_CategoryAndCursorAndPageable_When_GetPostsByCategory_Then_ReturnPostsResponseList() {
        //given
        var category = 1;
        var cursor = 1L;
        var pageable = PageRequest.of(0, 10);
        var posts = List.of(createPostDto(1L), createPostDto(2L), createPostDto(3L));

        given(postService.getPostDtosByCategory(category, cursor, pageable)).willReturn(posts);

        //when
        var actual = postFacade.getPostsByCategory(category, cursor, pageable);

        //then
        assertThat(actual)
                .isNotNull()
                .hasSize(posts.size())
                .isEqualTo(posts.stream().map(PostsResponse::of).collect(Collectors.toList()));
        then(postService).should(times(1)).getPostDtosByCategory(category, cursor, pageable);
    }

    @DisplayName("검색어, 커서를 받아 게시글 목록을 조회한다.")
    @Test
    void given_SearchWordAndCursor_When_GetPostsBySearchWord_Then_ReturnPostsResponseList() {
        //given
        var searchWord = "searchWord";
        var cursor = 1L;
        var posts = List.of(createPostDto(1L), createPostDto(2L), createPostDto(3L));
        given(postService.getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, 10))).willReturn(posts);

        //when
        var actual = postFacade.getPostsBySearchWord(searchWord, cursor);

        //then
        assertThat(actual)
                .isNotNull()
                .hasSize(posts.size())
                .isEqualTo(posts.stream().map(PostsResponse::of).collect(Collectors.toList()));
        then(postService).should(times(1)).getPostDtosBySearchWord(searchWord, cursor, PageRequest.of(0, 10));
    }

    @DisplayName("페이지 정보를 받아 인기 게시글 목록을 조회한다.")
    @Test
    void given_Page_When_GetHotPosts_Then_ReturnPostsWithPageResponse() {
        //given
        var page = 1;
        var posts = new PageImpl<>(List.of(createPostDto(1L), createPostDto(2L), createPostDto(3L)), PageRequest.of(page, 10), 3);
        var nextPage = 2;
        given(postService.getHotPostDtos(any())).willReturn(posts);
        given(postService.getNextPage(posts, page)).willReturn(nextPage);

        //when
        var actual = postFacade.getHotPosts(page);

        //then
        assertThat(actual)
                .isNotNull()
                .isEqualTo(PostsWithPageResponse.of(posts.stream().map(PostsResponse::of).collect(Collectors.toList()), nextPage));

        then(postService).should(times(1)).getHotPostDtos(PageRequest.of(page, 10));
        then(postService).should(times(1)).getNextPage(posts, page);
    }

    @DisplayName("유저 ID, 게시글 ID를 받아 게시글 좋아요를 생성한다.")
    @Test
    void given_UserIdAndPostId_When_CreatePostLike_Then_Success() {
        //given
        var userId = 1L;
        var postId = 1L;
        willDoNothing().given(postService).createPostLike(any());

        //when
        postFacade.createPostLike(userId, postId);

        //then
        then(postService).should(times(1)).createPostLike(any());
    }

    @DisplayName("유저 ID, 게시글 ID를 받아 게시글 좋아요를 삭제한다.")
    @Test
    void given_UserIdAndPostId_When_DeletePostLike_Then_Success() {
        //given
        var userId = 1L;
        var postId = 1L;
        willDoNothing().given(postService).deletePostLike(any());

        //when
        postFacade.deletePostLike(userId, postId);

        //then
        then(postService).should(times(1)).deletePostLike(any());
    }

    private UserDto createUserDto(Long id) {
        return DomainFixtureFactory.TestUserDto.createUserDto().id(id).build().get();
    }

    private PostDto createPostDto(Long id) {
        return DomainFixtureFactory.TestPostDto.createPostDto().id(id).build().get();
    }

    private PostRequest createPostRequest(Long id) {
        return PostRequest.of(id, "title", "content", 1);
    }

    private List<MultipartFile> createMultipartFiles() {
        return List.of(
                new MockMultipartFile("file", "file", "image/png", new byte[0])
        );
    }
}