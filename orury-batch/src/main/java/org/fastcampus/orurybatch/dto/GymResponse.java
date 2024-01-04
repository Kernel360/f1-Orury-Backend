package org.fastcampus.orurybatch.dto;

import org.fastcampus.orurydomain.gym.dto.GymDto;

public record GymResponse(
        String place_name,
        String road_address_name,
        String phone,
        String x,
        String y,
        String address_name,
        Boolean is_end
) {
    public static GymResponse of(
            String place_name,
            String road_address_name,
            String phone,
            String x,
            String y,
            String address_name,
            Boolean is_end
    ) {
        return new GymResponse(
                place_name,
                road_address_name,
                phone,
                x,
                y,
                address_name,
                is_end
        );
    }

    public GymDto toDto() {
        return GymDto.of(
                place_name,
                road_address_name,
                address_name,
                y,
                x,
                phone
        );
    }

}
