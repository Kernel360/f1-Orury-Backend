package org.orury.domain.gym.domain.dto;

import org.orury.domain.gym.domain.entity.GymLike;
import org.orury.domain.gym.domain.entity.GymLikePK;

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
