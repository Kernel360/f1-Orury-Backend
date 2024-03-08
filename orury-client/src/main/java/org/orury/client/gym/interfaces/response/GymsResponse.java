package org.orury.client.gym.interfaces.response;

import org.orury.domain.gym.domain.dto.GymDto;

import java.util.Objects;

public record GymsResponse(
        Long id,
        String name,
        String roadAddress,
        Float scoreAverage,
        int reviewCount,
        String thumbnailImage,
        Position position,
        boolean doingBusiness,
        boolean isLike
) {
    public static GymsResponse of(GymDto gymDto, boolean doingBusiness, boolean isLike) {
        return new GymsResponse(
                gymDto.id(),
                gymDto.name(),
                gymDto.roadAddress(),
                (gymDto.reviewCount() == 0) ? 0 : Math.round(gymDto.totalScore() * 10 / gymDto.reviewCount()) / 10f,
                gymDto.reviewCount(),
                (Objects.isNull(gymDto.images()) || gymDto.images().isEmpty()) ? null : gymDto.images().get(0),
                Position.of(gymDto.latitude(), gymDto.longitude()),
                doingBusiness,
                isLike
        );
    }

    private record Position(
            double latitude,
            double longitude
    ) {
        private static Position of(double latitude, double longitude) {
            return new Position(latitude, longitude);
        }
    }
}
