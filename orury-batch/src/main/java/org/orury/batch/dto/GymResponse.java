package org.orury.batch.dto;

import org.orury.domain.gym.domain.dto.GymDto;

public record GymResponse(
        String placeName,
        String kakaoId,
        String roadAddressName,
        String phone,
        String y,
        String x,
        String addressName
) {
    public static GymResponse of(
            String placeName,
            String kakaoId,
            String roadAddressName,
            String phone,
            String y,
            String x,
            String addressName
    ) {
        return new GymResponse(
                placeName,
                kakaoId,
                roadAddressName,
                phone,
                y,
                x,
                addressName
        );
    }

    public static GymResponse from(KakaoMapGymResponse.Documents docs) {
        return GymResponse.of(
                docs.getPlaceName(),
                docs.getId(),
                docs.getRoadAddressName(),
                docs.getPhone(),
                docs.getY(),
                docs.getX(),
                docs.getAddressName()
        );
    }

    public GymDto toDto() {
        return GymDto.of(
                null,
                placeName,
                kakaoId,
                roadAddressName,
                addressName,
                0,
                0,
                0,
                null,
                Float.parseFloat(y),
                Float.parseFloat(x),
                null,
                phone,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

}
