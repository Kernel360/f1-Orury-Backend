package org.orury.client.gym.interfaces.response;

import org.orury.domain.global.constants.Constants;
import org.orury.domain.gym.domain.dto.GymDto;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record GymResponse(
        Long id,
        String name,
        String roadAddress,
        String address,
        Float scoreAverage,
        int reviewCount,
        List<String> images,
        Position position,
        String brand,
        String phoneNumber,
        String kakaoMapLink,
        String instagramLink,
        String homepageLink,
        String settingDay,
        Set<Map.Entry<String, String>> businessHours,
        boolean doingBusiness,
        boolean isLike,
        String gymType,
        List<GymReviewStatistics.TotalReviewChart.ReviewCount> barChartData,
        List<GymReviewStatistics.MonthlyReviewChart.MonthlyReviewCount> lineChartData
) {
    public static GymResponse of(
            GymDto gymDto,
            boolean doingBusiness,
            boolean isLike,
            GymReviewStatistics gymReviewStatistics
    ) {
        Set<Map.Entry<String, String>> koreanBusinessHours = gymDto.businessHours().entrySet().stream()
                .map(entry -> Map.entry(entry.getKey().getDisplayName(TextStyle.NARROW, Locale.KOREAN), entry.getValue()))
                .collect(Collectors.toSet());

        return new GymResponse(
                gymDto.id(),
                gymDto.name(),
                gymDto.roadAddress(),
                gymDto.address(),
                (gymDto.reviewCount() == 0) ? 0 : Math.round(gymDto.totalScore() * 10 / gymDto.reviewCount()) / 10f,
                gymDto.reviewCount(),
                gymDto.images(),
                Position.of(gymDto.latitude(), gymDto.longitude()),
                gymDto.brand(),
                gymDto.phoneNumber(),
                Constants.KAKAO_MAP_BASE_URL.getMessage() + gymDto.kakaoId(),
                gymDto.instagramLink(),
                gymDto.homepageLink(),
                gymDto.settingDay(),
                koreanBusinessHours,
                doingBusiness,
                isLike,
                gymDto.gymType().getDescription(),
                gymReviewStatistics.barChartData(),
                gymReviewStatistics.lineChartData()
        );
    }

    private record Position(
            double latitude,
            double longitude
    ) {
        private static Position of(double latitude, double longitude) {
            return new Position(latitude, longitude);
        }
    }
}

