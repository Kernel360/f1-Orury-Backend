package org.fastcampus.orurybatch.dto;

import org.fastcampus.orurydomain.gym.dto.GymDto;

public record GymResponse(
        String placeName,
        String kakaoId,
        String roadAddressName,
        String phone,
        String x,
        String y,
        String addressName
) {
    public static GymResponse of(
            String placeName,
            String kakaoId,
            String roadAddressName,
            String phone,
            String x,
            String y,
            String addressName
    ) {
        return new GymResponse(
                placeName,
                kakaoId,
                roadAddressName,
                phone,
                x,
                y,
                addressName
        );
    }

    public static GymResponse from(KakaoMapGymResponse.Documents docs) {
        return GymResponse.of(
                docs.getPlaceName(),
                docs.getId(),
                docs.getRoadAddressName(),
                docs.getPhone(),
                docs.getX(),
                docs.getY(),
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
                null,
                0,
                0,
                null,
                x,
                y,
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
