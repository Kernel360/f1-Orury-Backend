package org.fastcampus.oruryclient.gym.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.time.Month;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record GymReviewStatistics(
        List<TotalReviewChart.ReviewCount> barChartData,
        List<MonthlyReviewChart.MonthlyReviewCount> lineChartData
) {
    public static GymReviewStatistics of(List<ReviewDto> reviewDtos) {
        return new GymReviewStatistics(
                TotalReviewChart.of(reviewDtos),
                MonthlyReviewChart.of(reviewDtos)
        );
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record TotalReviewChart(
            List<ReviewCount> barChartData
    ) {
        private static List<ReviewCount> of(List<ReviewDto> reviewDtos) {
            Map<Float, Integer> counts = new LinkedHashMap<>();
            for (ReviewDto review : reviewDtos) {
                counts.put(review.score(), counts.getOrDefault(review.score(), 0) + 1);
            }

            return counts.entrySet().stream()
                    .map(o -> new ReviewCount(o.getKey(), o.getValue()))
                    .toList();
        }

        record ReviewCount(float point, int count) {
        }
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record MonthlyReviewChart(
            List<MonthlyReviewCount> lineChartData
    ) {
        private static List<MonthlyReviewCount> of(List<ReviewDto> reviewDtos) {

            EnumMap<Month, Object[]> counts = new EnumMap<>(Month.class);

            for (ReviewDto review : reviewDtos) {
                float tempTotal = (float) counts.getOrDefault(review.createdAt().getMonth(), new Object[]{0f, 0})[0] + review.score();
                int tempCount = (int) counts.getOrDefault(review.createdAt().getMonth(), new Object[]{0f, 0})[1] + 1;
                Object[] value = new Object[]{tempTotal, tempCount};
                counts.put(review.createdAt().getMonth(), value);
            }

            return counts.entrySet().stream()
                    .map(o -> new MonthlyReviewCount(
                            o.getKey().getValue(),
                            (float) o.getValue()[0] / (int) o.getValue()[1],
                            (int) o.getValue()[1]))
                    .toList();
        }

        record MonthlyReviewCount(int month, float avg, int count) {
        }
    }
}
