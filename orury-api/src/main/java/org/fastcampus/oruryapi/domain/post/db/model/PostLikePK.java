package org.fastcampus.oruryapi.domain.post.db.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fastcampus.oruryapi.domain.user.db.model.User;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PostLikePK implements Serializable {
    private Long userId;
    private Long postId;

    private PostLikePK(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public PostLikePK of(User user, Post post) {
        return new PostLikePK(
                user.getId(),
                post.getId()
        );
    }
}
