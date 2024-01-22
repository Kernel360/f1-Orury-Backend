package org.fastcampus.oruryclient.gym.converter.response;

import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.gym.dto.GymDto;

public record GymsResponse(
        Long id,
        String name,
        String roadAddress,
        Float scoreAverage,
        String thumbnailImage,
        LocationGrid position,
        boolean isOperating,
        boolean isLike
) {
    public static GymsResponse of(GymDto gymDto, boolean isOperating, boolean isLike) {
        return new GymsResponse(
                gymDto.id(),
                gymDto.name(),
                gymDto.roadAddress(),
                gymDto.scoreAverage(),
                ImageUrlConverter.convertToList(gymDto.images()).get(0),
                LocationGrid.of(gymDto.latitude(), gymDto.longitude()),
                isOperating,
                isLike
        );
    }
}
