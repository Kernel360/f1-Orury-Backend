package org.orury.domain.gym.domain.dto;


import org.orury.domain.gym.domain.entity.Gym;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;

/**
 * DTO for {@link Gym}
 */
public record GymDto(
        Long id,
        String name,
        String kakaoId,
        String roadAddress,
        String address,
        float totalScore,
        int reviewCount,
        int likeCount,
        List<String> images,
        double latitude,
        double longitude,
        String brand,
        String phoneNumber,
        String instagramLink,
        String settingDay,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        EnumMap<DayOfWeek, String> businessHours,
        String homepageLink,
        String remark,
        GymType gymType
) {
    public static GymDto of(
            Long id,
            String name,
            String kakaoId,
            String roadAddress,
            String address,
            float totalScore,
            int reviewCount,
            int likeCount,
            List<String> images,
            double latitude,
            double longitude,
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
            String remark,
            GymType gymType
    ) {
        return new GymDto(
                id,
                name,
                kakaoId,
                roadAddress,
                address,
                totalScore,
                reviewCount,
                likeCount,
                images,
                latitude,
                longitude,
                brand,
                phoneNumber,
                instagramLink,
                settingDay,
                createdAt,
                updatedAt,
                createBusinessHoursMap(serviceMon, serviceTue, serviceWed, serviceThu, serviceFri, serviceSat, serviceSun),
                homepageLink,
                remark,
                gymType
        );
    }

    public Gym toEntity() {
        return Gym.of(
                this.id,
                this.name,
                this.kakaoId,
                this.roadAddress,
                this.address,
                this.totalScore,
                this.reviewCount,
                this.likeCount,
                this.images,
                this.latitude,
                this.longitude,
                this.brand,
                this.phoneNumber,
                this.instagramLink,
                this.settingDay,
                this.businessHours.get(DayOfWeek.MONDAY),
                this.businessHours.get(DayOfWeek.TUESDAY),
                this.businessHours.get(DayOfWeek.WEDNESDAY),
                this.businessHours.get(DayOfWeek.THURSDAY),
                this.businessHours.get(DayOfWeek.FRIDAY),
                this.businessHours.get(DayOfWeek.SATURDAY),
                this.businessHours.get(DayOfWeek.SUNDAY),
                this.homepageLink,
                this.remark,
                this.createdAt,
                this.updatedAt,
                this.gymType
        );
    }

    public static GymDto from(Gym entity) {
        return GymDto.of(
                entity.getId(),
                entity.getName(),
                entity.getKakaoId(),
                entity.getRoadAddress(),
                entity.getAddress(),
                entity.getTotalScore(),
                entity.getReviewCount(),
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
                entity.getRemark(),
                entity.getGymType()
        );
    }

    public static GymDto from(Gym entity, List<String> urls) {
        return GymDto.of(
                entity.getId(),
                entity.getName(),
                entity.getKakaoId(),
                entity.getRoadAddress(),
                entity.getAddress(),
                entity.getTotalScore(),
                entity.getReviewCount(),
                entity.getLikeCount(),
                urls,
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
                entity.getRemark(),
                entity.getGymType()
        );
    }

    private static EnumMap<DayOfWeek, String> createBusinessHoursMap(
            String serviceMon,
            String serviceTue,
            String serviceWed,
            String serviceThu,
            String serviceFri,
            String serviceSat,
            String serviceSun
    ) {
        EnumMap<DayOfWeek, String> businessHoursMap = new EnumMap<>(DayOfWeek.class);
        businessHoursMap.put(DayOfWeek.MONDAY, serviceMon);
        businessHoursMap.put(DayOfWeek.TUESDAY, serviceTue);
        businessHoursMap.put(DayOfWeek.WEDNESDAY, serviceWed);
        businessHoursMap.put(DayOfWeek.THURSDAY, serviceThu);
        businessHoursMap.put(DayOfWeek.FRIDAY, serviceFri);
        businessHoursMap.put(DayOfWeek.SATURDAY, serviceSat);
        businessHoursMap.put(DayOfWeek.SUNDAY, serviceSun);
        return businessHoursMap;
    }
}