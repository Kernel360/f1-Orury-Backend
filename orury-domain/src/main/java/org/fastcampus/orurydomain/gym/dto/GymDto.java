package org.fastcampus.orurydomain.gym.dto;


import org.fastcampus.orurydomain.gym.db.model.Gym;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.EnumMap;

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
        int likeCount,
        String images,
        double latitude,
        double longitude,
        String brand,
        String phoneNumber,
        String instagramLink,
        String settingDay,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        EnumMap<DayOfWeek, String> operatingTimes,
        String homepageLink,
        String remark
) {
    public static GymDto of(
            Long id,
            String name,
            String kakaoId,
            String roadAddress,
            String address,
            Float scoreAverage,
            int likeCount,
            String images,
            String latitude,
            String longitude,
            String brand,
            String phoneNumber,
            String instagramLink,
            String settingDay,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            String serviceMon,
            String serviceTue,
            String serviceWed,
            String serviceThu,
            String serviceFri,
            String serviceSat,
            String serviceSun,
            String homepageLink,
            String remark
    ) {
        return new GymDto(
                id,
                name,
                kakaoId,
                roadAddress,
                address,
                scoreAverage,
                likeCount,
                images,
                Double.parseDouble(latitude),
                Double.parseDouble(longitude),
                brand,
                phoneNumber,
                instagramLink,
                settingDay,
                createdAt,
                updatedAt,
                createOperatingTimeMap(serviceMon, serviceTue, serviceWed, serviceThu, serviceFri, serviceSat, serviceSun),
                homepageLink,
                remark
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
                this.likeCount,
                this.images,
                String.valueOf(this.latitude),
                String.valueOf(this.longitude),
                this.brand,
                this.phoneNumber,
                this.instagramLink,
                this.settingDay,
                this.operatingTimes.get(DayOfWeek.MONDAY),
                this.operatingTimes.get(DayOfWeek.TUESDAY),
                this.operatingTimes.get(DayOfWeek.WEDNESDAY),
                this.operatingTimes.get(DayOfWeek.THURSDAY),
                this.operatingTimes.get(DayOfWeek.FRIDAY),
                this.operatingTimes.get(DayOfWeek.SATURDAY),
                this.operatingTimes.get(DayOfWeek.SUNDAY),
                this.homepageLink,
                this.remark
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
                entity.getLikeCount(),
                entity.getImages(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getBrand(),
                entity.getPhoneNumber(),
                entity.getInstagramLink(),
                entity.getSettingDay(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getServiceMon(),
                entity.getServiceTue(),
                entity.getServiceWed(),
                entity.getServiceThu(),
                entity.getServiceFri(),
                entity.getServiceSat(),
                entity.getServiceSun(),
                entity.getHomepageLink(),
                entity.getRemark()
        );
    }

    private static EnumMap<DayOfWeek, String> createOperatingTimeMap(
            String serviceMon,
            String serviceTue,
            String serviceWed,
            String serviceThu,
            String serviceFri,
            String serviceSat,
            String serviceSun
    ) {
        EnumMap<DayOfWeek, String> operatingMap = new EnumMap<>(DayOfWeek.class);
        operatingMap.put(DayOfWeek.MONDAY, serviceMon);
        operatingMap.put(DayOfWeek.TUESDAY, serviceTue);
        operatingMap.put(DayOfWeek.WEDNESDAY, serviceWed);
        operatingMap.put(DayOfWeek.THURSDAY, serviceThu);
        operatingMap.put(DayOfWeek.FRIDAY, serviceFri);
        operatingMap.put(DayOfWeek.SATURDAY, serviceSat);
        operatingMap.put(DayOfWeek.SUNDAY, serviceSun);
        return operatingMap;
    }
}