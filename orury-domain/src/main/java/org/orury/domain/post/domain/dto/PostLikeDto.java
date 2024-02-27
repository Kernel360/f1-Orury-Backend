package org.orury.domain.post.dto;

import org.orury.domain.post.db.PostLike;
import org.orury.domain.post.db.PostLikePK;

/**
 * DTO for {@link PostLike}
 */
public record PostLikeDto(
        PostLikePK postLikePK
) {
    public static PostLikeDto of(
            PostLikePK postLikePK
    ) {
        return new PostLikeDto(
                postLikePK
        );
    }

    public static PostLikeDto from(PostLike entity) {
        return PostLikeDto.of(
                entity.getPostLikePK()
        );
    }

    public PostLike toEntity() {
        return PostLike.of(
                postLikePK
        );
    }
}