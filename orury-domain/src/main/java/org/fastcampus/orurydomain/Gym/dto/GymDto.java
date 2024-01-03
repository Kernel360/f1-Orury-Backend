package org.fastcampus.orurydomain.Gym.dto;

import org.fastcampus.orurydomain.Gym.db.model.Gym;

import java.time.LocalDateTime;

/**
 * DTO for {@link org.fastcampus.orurydomain.Gym.db.model.Gym}
 */
public record GymDto(
        Long id,
        String name,
        String roadAddress,
        String address,
        Float scoreAverage,
        String images,
        String latitude,
        String longitude,
        String openTime,
        String closeTime,
        String brand,
        String phoneNumber,
        String instagramLink,
        String settingDay,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static GymDto of(
            Long id,
            String name,
            String roadAddress,
            String address,
            Float scoreAverage,
            String images,
            String latitude,
            String longitude,
            String openTime,
            String closeTime,
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
                roadAddress,
                address,
                scoreAverage,
                images,
                latitude,
                longitude,
                openTime,
                closeTime,
                brand,
                phoneNumber,
                instagramLink,
                settingDay,
                createdAt,
                updatedAt
        );
    }

    public static GymDto from(Gym entity) {
        return GymDto.of(
                entity.getId(),
                entity.getName(),
                entity.getRoadAddress(),
                entity.getAddress(),
                entity.getScoreAverage(),
                entity.getImages(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getOpenTime(),
                entity.getCloseTime(),
                entity.getBrand(),
                entity.getPhoneNumber(),
                entity.getInstagramLink(),
                entity.getSettingDay(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Gym toEntity() {
        return Gym.of(
                this.name,
                this.roadAddress,
                this.address,
                this.scoreAverage,
                this.images,
                this.latitude,
                this.longitude,
                this.openTime,
                this.closeTime,
                this.brand,
                this.phoneNumber,
                this.instagramLink,
                this.settingDay
        );
    }
}