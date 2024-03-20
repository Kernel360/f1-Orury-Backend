package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.comment.domain.dto.CommentLikeDto;
import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.comment.domain.entity.CommentLike;
import org.orury.domain.comment.domain.entity.CommentLikePK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;

public class CommentDomainFixture {

    private CommentDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestComment {
        private @Builder.Default Long id = 87342L;
        private @Builder.Default String content = "댓글내용";
        private @Builder.Default Long parentId = 12L;
        private @Builder.Default int likeCount = 3;
        private @Builder.Default Post post = PostDomainFixture.TestPost.createPost().build().get();
        private @Builder.Default User user = UserDomainFixture.TestUser.createUser().build().get();
        private @Builder.Default int deleted = NumberConstants.IS_NOT_DELETED;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestComment.TestCommentBuilder createComment() {
            return TestComment.builder();
        }

        public static TestComment.TestCommentBuilder createComment(Long commentId) {
            return TestComment.builder().id(commentId);
        }

        public static TestComment.TestCommentBuilder createParentComment() {
            return TestComment.builder().parentId(0L);
        }

        public static TestComment.TestCommentBuilder createChildComment() {
            return TestComment.builder().parentId(11524L);
        }

        public static TestComment.TestCommentBuilder createDeletedComment() {
            return TestComment.builder().deleted(NumberConstants.IS_DELETED);
        }

        public Comment get() {
            return mapper.convertValue(this, Comment.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentDto {
        private @Builder.Default Long id = 87342L;
        private @Builder.Default String content = "댓글내용";
        private @Builder.Default Long parentId = 112L;
        private @Builder.Default int likeCount = 31;
        private @Builder.Default PostDto postDto = PostDomainFixture.TestPostDto.createPostDto().build().get();
        private @Builder.Default UserDto userDto = UserDomainFixture.TestUserDto.createUserDto().build().get();
        private @Builder.Default int deleted = NumberConstants.IS_NOT_DELETED;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestCommentDto.TestCommentDtoBuilder createCommentDto() {
            return TestCommentDto.builder();
        }

        public static TestCommentDto.TestCommentDtoBuilder createCommentDto(Long commentId) {
            return TestCommentDto.builder().id(commentId);
        }

        public static TestCommentDto.TestCommentDtoBuilder createParentCommentDto() {
            return TestCommentDto.builder().parentId(0L);
        }

        public static TestCommentDto.TestCommentDtoBuilder createChildCommentDto(Long parentId) {
            return TestCommentDto.builder().parentId(parentId);
        }

        public static TestCommentDto.TestCommentDtoBuilder createDeletedCommentDto() {
            return TestCommentDto.builder().deleted(NumberConstants.IS_DELETED);
        }

        public CommentDto get() {
            return mapper.convertValue(this, CommentDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentLikePK {
        private @Builder.Default Long commentId = 1234L;
        private @Builder.Default Long userId = 5678L;

        public static TestCommentLikePK.TestCommentLikePKBuilder createCommentLikePK() {
            return TestCommentLikePK.builder();
        }

        public CommentLikePK get() {
            return mapper.convertValue(this, CommentLikePK.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentLike {
        private @Builder.Default CommentLikePK commentLikePK = TestCommentLikePK.createCommentLikePK().build().get();

        public static TestCommentLike.TestCommentLikeBuilder createCommentLike() {
            return TestCommentLike.builder();
        }

        public static TestCommentLike.TestCommentLikeBuilder createCommentLike(Long commentId, Long userId) {
            return TestCommentLike.builder().commentLikePK(
                    TestCommentLikePK.createCommentLikePK()
                            .commentId(commentId)
                            .userId(userId).build().get()
            );
        }

        public CommentLike get() {
            return mapper.convertValue(this, CommentLike.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentLikeDto {
        private @Builder.Default CommentLikePK commentLikePK = TestCommentLikePK.createCommentLikePK().build().get();

        public static TestCommentLikeDto.TestCommentLikeDtoBuilder createCommentLikeDto() {
            return TestCommentLikeDto.builder();
        }

        public static TestCommentLikeDto.TestCommentLikeDtoBuilder createCommentLikeDto(CommentLikePK commentLikePK) {
            return TestCommentLikeDto.builder().commentLikePK(commentLikePK);
        }

        public static TestCommentLikeDto.TestCommentLikeDtoBuilder createCommentLikeDto(Long commentId, Long userId) {
            return TestCommentLikeDto.builder()
                    .commentLikePK(TestCommentLikePK.createCommentLikePK()
                            .commentId(commentId)
                            .userId(userId).build().get()
                    );
        }

        public CommentLikeDto get() {
            return mapper.convertValue(this, CommentLikeDto.class);
        }
    }
}
