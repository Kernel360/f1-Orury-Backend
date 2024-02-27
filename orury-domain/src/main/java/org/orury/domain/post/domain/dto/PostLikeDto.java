package org.orury.domain.post.domain.dto;

import org.orury.domain.post.domain.entity.PostLike;
import org.orury.domain.post.domain.entity.PostLikePK;

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