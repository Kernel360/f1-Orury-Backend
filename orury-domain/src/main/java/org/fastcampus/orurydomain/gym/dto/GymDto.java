package org.fastcampus.orurydomain.gym.dto;


import java.time.LocalDateTime;

/**
 * DTO for {@link org.fastcampus.orurydomain.gym.db.model.Gym}
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
    public static GymDto of(String place_name, String road_address_name, String phone, String x, String y, String address_name) {
        return GymDto.of(null, place_name, road_address_name, address_name, null, null, x, y, null, null, null, phone, null, null, null, null);
    }

    public static GymDto of(Long id, String name, String roadAddress, String address, Float scoreAverage, String images, String latitude, String longitude, String openTime, String closeTime, String brand, String phoneNumber, String instagramLink, String settingDay, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new GymDto(id, name, roadAddress, address, scoreAverage, images, latitude, longitude, openTime, closeTime, brand, phoneNumber, instagramLink, settingDay, createdAt, updatedAt);
    }
}