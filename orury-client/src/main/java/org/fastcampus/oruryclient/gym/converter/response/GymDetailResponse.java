package org.fastcampus.oruryclient.gym.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GymDetailResponse(
        Long id,
        String name,
        String roadAddress,
        String address,
        Float scoreAverage,
        List<String> images,
        Position position,
        String brand,
        String phoneNumber,
        String instagramLink,
        String homepageLink,
        String settingDay,
        Set<Map.Entry<DayOfWeek, String>> businessHours,
        boolean doingBusiness,
        boolean isLike,
        GymDetailStatistics.TotalReviewChart barChartData,
        GymDetailStatistics.MonthlyReviewChart lineChartData
) {
    public static GymDetailResponse of(
            GymDto gymDto,
            boolean doingBusiness,
            boolean isLike,
            List<ReviewDto> reviewDtos
    ) {
        return new GymDetailResponse(
                gymDto.id(),
                gymDto.name(),
                gymDto.roadAddress(),
                gymDto.address(),
                gymDto.scoreAverage(),
                ImageUrlConverter.convertToList(gymDto.images()),
                Position.of(gymDto.latitude(), gymDto.longitude()),
                gymDto.brand(),
                gymDto.phoneNumber(),
                gymDto.instagramLink(),
                gymDto.homepageLink(),
                gymDto.settingDay(),
                gymDto.businessHours().entrySet(),
                doingBusiness,
                isLike,
                GymDetailStatistics.TotalReviewChart.of(reviewDtos),
                GymDetailStatistics.MonthlyReviewChart.of(reviewDtos)
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

