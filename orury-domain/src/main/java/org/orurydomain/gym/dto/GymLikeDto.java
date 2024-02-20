package org.orurydomain.gym.dto;

import org.orurydomain.gym.db.model.GymLike;
import org.orurydomain.gym.db.model.GymLikePK;

public record GymLikeDto(
        GymLikePK gymLikePK
) {
    private static GymLikeDto of(GymLikePK gymLikePK) {
        return new GymLikeDto(gymLikePK);
    }

    public static GymLikeDto from(GymLike entity) {
        return GymLikeDto.of(entity.getGymLikePK());
    }

    public GymLike toEntity() {
        return GymLike.of(gymLikePK);
    }
}
