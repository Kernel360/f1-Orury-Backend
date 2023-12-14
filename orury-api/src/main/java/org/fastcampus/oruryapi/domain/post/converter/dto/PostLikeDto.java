package org.fastcampus.oruryapi.domain.post.converter.dto;

import org.fastcampus.oruryapi.domain.post.db.model.PostLike;
import org.fastcampus.oruryapi.domain.post.db.model.PostLikePK;

/**
 * DTO for {@link PostLike}
 */
public record PostLikeDto(
        PostLikePK postLikePK
) {
    private static PostLikeDto of(
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