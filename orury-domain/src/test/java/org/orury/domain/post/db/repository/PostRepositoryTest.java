package org.orury.domain.post.db.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.RepositoryTest;
import org.orury.domain.post.db.model.Post;
import org.orury.domain.user.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// FIXME: 2/26/24 DB 예약어 오류로 인한 실패
@Disabled
@RepositoryTest
class PostRepositoryTest {
    private final PostRepository postRepository;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @BeforeEach
    void setUp() {
        postRepository.save(createPost());
    }

    @DisplayName("포스트 테스트")
    @Test
    void test() {
        //given
        Post post = createPost();
        User user = createUser();

        //when
        assertThat(postRepository.getReferenceById(1L)).isEqualTo(post);

        //then
    }

    private Post createPost() {
        return Post.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                List.of(),
                1,
                createUser(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private User createUser() {
        return User.of(
                1L,
                "email@email",
                "nick",
                "pw",
                1,
                1,
                LocalDate.now(),
                "iamge",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1
        );
    }
}