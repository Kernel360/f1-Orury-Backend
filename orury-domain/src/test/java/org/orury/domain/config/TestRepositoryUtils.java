package org.orury.domain.config;

import org.orury.domain.comment.domain.entity.Comment;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestRepositoryUtils {
    protected User createUser(Long userId) {
        return User.of(
                userId,
                "email@email",
                "nick",
                "pw",
                1,
                1,
                LocalDate.now(),
                "profile",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0
        );
    }

    protected Post createPost(Long postId, User user) {
        return Post.of(
                postId,
                "title",
                "content",
                0,
                0,
                0,
                List.of("image1", "image2"),
                1,
                user,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    protected Comment createComment(
            Long parentId,
            Long commentId,
            Post post,
            User user
    ) {
        return Comment.of(
                commentId,
                "content",
                parentId,
                0,
                post,
                user,
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    protected Gym createGym() {
        return Gym.of(
                1L,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                4.5f,
                12,
                11,
                List.of("image1,image2"),
                "37.513709",
                "127.062144",
                "더클라임",
                "01012345678",
                "gymInstagramLink.com",
                "MONDAY",
                "11:11-23:11",
                "11:22-23:22",
                "11:33-23:33",
                "11:44-23:44",
                "11:55-23:55",
                "11:66-23:66",
                "11:77-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    protected Review createReview(Long reviewId, User user) {
        return Review.of(
                reviewId,
                "content",
                List.of("image1", "image2"),
                0.0f,
                0,
                0,
                0,
                0,
                0,
                user,
                createGym(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
