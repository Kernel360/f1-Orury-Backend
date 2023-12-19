package org.fastcampus.oruryapi.domain.post.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PostLikePK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    private PostLikePK(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public static PostLikePK of(Long userId, Long postId) {
        return new PostLikePK(
                userId,
                postId
        );
    }
}
