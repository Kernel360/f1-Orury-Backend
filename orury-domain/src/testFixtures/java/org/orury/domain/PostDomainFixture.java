package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.dto.PostLikeDto;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLike;
import org.orury.domain.post.domain.entity.PostLikePK;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class PostDomainFixture {

    private PostDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestPost {
        private @Builder.Default Long id = 847912L;
        private @Builder.Default String title = "postTitle";
        private @Builder.Default String content = "postContent";
        private @Builder.Default int viewCount = 0;
        private @Builder.Default int commentCount = 0;
        private @Builder.Default int likeCount = 0;
        private @Builder.Default List<String> images = List.of("postImage");
        private @Builder.Default int category = 1;
        private @Builder.Default User user = UserDomainFixture.TestUser.createUser(51241L).build().get();
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestPost.TestPostBuilder createPost() {
            return TestPost.builder();
        }

        public static TestPost.TestPostBuilder createPost(Long postId) {
            return TestPost.builder().id(postId);
        }

        public Post get() {
            return mapper.convertValue(this, Post.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostDto {
        private @Builder.Default Long id = 123456L;
        private @Builder.Default String title = "postTitle";
        private @Builder.Default String content = "postContent";
        private @Builder.Default int viewCount = 0;
        private @Builder.Default int commentCount = 0;
        private @Builder.Default int likeCount = 0;
        private @Builder.Default List<String> images = List.of();
        private @Builder.Default int category = 1;
        private @Builder.Default UserDto userDto = UserDomainFixture.TestUserDto.createUserDto(149009L).build().get();
        private @Builder.Default Boolean isLike = false;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestPostDto.TestPostDtoBuilder createPostDto() {
            return TestPostDto.builder();
        }

        public static TestPostDto.TestPostDtoBuilder createPostDto(Long postId) {
            return TestPostDto.builder().id(postId);
        }

        public PostDto get() {
            return mapper.convertValue(this, PostDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostLikePK {
        private @Builder.Default Long postId = 5269L;
        private @Builder.Default Long userId = 24579L;

        public static TestPostLikePK.TestPostLikePKBuilder createPostLikePK() {
            return TestPostLikePK.builder();
        }

        public PostLikePK get() {
            return mapper.convertValue(this, PostLikePK.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostLike {
        private @Builder.Default PostLikePK postLikePK = TestPostLikePK.createPostLikePK().build().get();

        public static TestPostLike.TestPostLikeBuilder createPostLike() {
            return TestPostLike.builder();
        }

        public static TestPostLike.TestPostLikeBuilder createPostLike(Long postId, Long userId) {
            return TestPostLike.builder().postLikePK(
                    TestPostLikePK.createPostLikePK()
                            .postId(postId)
                            .userId(userId).build().get()
            );
        }

        public PostLike get() {
            return mapper.convertValue(this, PostLike.class);
        }
    }

    @Getter
    @Builder
    public static class TestPostLikeDto {
        private @Builder.Default PostLikePK postLikePK = TestPostLikePK.createPostLikePK().build().get();

        public static TestPostLikeDto.TestPostLikeDtoBuilder createPostLikeDto() {
            return TestPostLikeDto.builder();
        }

        public static TestPostLikeDto.TestPostLikeDtoBuilder createPostLikeDto(PostLikePK postLikePK) {
            return TestPostLikeDto.builder().postLikePK(postLikePK);
        }

        public static TestPostLikeDto.TestPostLikeDtoBuilder createPostLikeDto(Long postId, Long userId) {
            return TestPostLikeDto.builder()
                    .postLikePK(TestPostLikePK.createPostLikePK()
                            .postId(postId)
                            .userId(userId).build().get()
                    );
        }

        public PostLikeDto get() {
            return mapper.convertValue(this, PostLikeDto.class);
        }
    }
}
