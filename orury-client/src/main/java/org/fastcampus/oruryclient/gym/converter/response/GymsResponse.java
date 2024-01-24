package org.fastcampus.oruryclient.gym.converter.response;

import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.gym.dto.GymDto;

public record GymsResponse(
        Long id,
        String name,
        String roadAddress,
        Float scoreAverage,
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
                gymDto.scoreAverage(),
                ImageUrlConverter.convertToList(gymDto.images()).get(0),
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
