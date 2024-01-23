package org.fastcampus.orurydomain.gym.dto;


import org.fastcampus.orurydomain.gym.db.model.Gym;

import java.time.LocalDateTime;

/**
 * DTO for {@link org.fastcampus.orurydomain.gym.db.model.Gym}
 */
public record GymDto(
        Long id,
        String name,
        String kakaoId,
        String roadAddress,
        String address,
        Float scoreAverage,
        String images,
        String latitude,
        String longitude,
//        String openTime,
//        String closeTime,
        String brand,
        String phoneNumber,
        String instagramLink,
        String settingDay,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static GymDto of(
            String place_name,
            String kakaoId,
            String road_address_name,
            String phone,
            String x,
            String y,
            String address_name
    ) {
        return GymDto.of(
                null,
                place_name,
                kakaoId,
                road_address_name,
                address_name,
                null,
                null,
                x,
                y,
//                null,
//                null,
                null,
                phone,
                null,
                null,
                null,
                null
        );
    }

    public static GymDto of(
            Long id,
            String name,
            String kakaoId,
            String roadAddress,
            String address,
            Float scoreAverage,
            String images,
            String latitude,
            String longitude,
//            String openTime,
//            String closeTime,
            String brand,
            String phoneNumber,
            String instagramLink,
            String settingDay,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new GymDto(
                id,
                name,
                kakaoId,
                roadAddress,
                address,
                scoreAverage,
                images,
                latitude,
                longitude,
//                openTime,
//                closeTime,
                brand,
                phoneNumber,
                instagramLink,
                settingDay,
                createdAt,
                updatedAt
        );
    }

    public Gym toEntity() {
        return Gym.of(
                this.id,
                this.name,
                this.kakaoId,
                this.roadAddress,
                this.address,
                this.scoreAverage,
                this.images,
                this.latitude,
                this.longitude,
//                this.openTime,
//                this.closeTime,
                this.brand,
                this.phoneNumber,
                this.instagramLink,
                this.settingDay
        );
    }

    public static GymDto from(Gym entity) {
        return GymDto.of(
                entity.getId(),
                entity.getName(),
                entity.getKakaoId(),
                entity.getRoadAddress(),
                entity.getAddress(),
                entity.getScoreAverage(),
                entity.getImages(),
                entity.getLatitude(),
                entity.getLongitude(),
//                entity.getOpenTime(),
//                entity.getCloseTime(),
                entity.getBrand(),
                entity.getPhoneNumber(),
                entity.getInstagramLink(),
                entity.getSettingDay(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}