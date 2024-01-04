package org.fastcampus.orurybatch.dto;

import org.fastcampus.orurydomain.gym.dto.GymDto;

public record GymResponse(
        String place_name,
        String kakaoId,
        String road_address_name,
        String phone,
        String x,
        String y,
        String address_name
) {
    public static GymResponse of(
            String place_name,
            String kakaoId,
            String road_address_name,
            String phone,
            String x,
            String y,
            String address_name
    ) {
        return new GymResponse(
                place_name,
                kakaoId,
                road_address_name,
                phone,
                x,
                y,
                address_name
        );
    }

    public static GymResponse from(KakaoMapGymResponse.Documents docs) {
        return GymResponse.of(
                docs.getPlace_name(),
                docs.getId(),
                docs.getRoad_address_name(),
                docs.getPhone(),
                docs.getX(),
                docs.getY(),
                docs.getAddress_name()
        );
    }

    public GymDto toDto() {
        return GymDto.of(
                place_name,
                kakaoId,
                road_address_name,
                phone,
                x,
                y,
                address_name
        );
    }

}
