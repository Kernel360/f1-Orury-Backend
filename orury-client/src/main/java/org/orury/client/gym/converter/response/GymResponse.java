package org.orury.client.gym.converter.response;

import org.orury.domain.global.constants.Constants;
import org.orury.domain.gym.dto.GymDto;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Set<Map.Entry<DayOfWeek, String>> businessHours,
        boolean doingBusiness,
        boolean isLike,
        List<GymReviewStatistics.TotalReviewChart.ReviewCount> barChartData,
        List<GymReviewStatistics.MonthlyReviewChart.MonthlyReviewCount> lineChartData
) {
    public static GymResponse of(
            GymDto gymDto,
            boolean doingBusiness,
            boolean isLike,
            GymReviewStatistics gymReviewStatistics
    ) {
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
                gymDto.businessHours()
                        .entrySet(),
                doingBusiness,
                isLike,
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

