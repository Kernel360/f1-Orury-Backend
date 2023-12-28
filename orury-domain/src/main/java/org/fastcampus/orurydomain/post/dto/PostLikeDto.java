package org.fastcampus.orurydomain.post.dto;

import org.fastcampus.orurydomain.post.db.model.PostLike;
import org.fastcampus.orurydomain.post.db.model.PostLikePK;

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