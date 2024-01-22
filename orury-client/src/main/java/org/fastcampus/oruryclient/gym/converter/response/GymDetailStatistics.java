package org.fastcampus.oruryclient.gym.converter.response;

import org.fastcampus.orurydomain.review.dto.ReviewDto;

import java.time.Month;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record GymDetailStatistics(
        TotalReviewChart barChartData,
        MonthlyReviewChart lineChartData
) {
    record TotalReviewChart(
            List<ReviewCount> barChartData
    ) {
        public static TotalReviewChart of(List<ReviewDto> reviewDtos) {
            Map<Float, Integer> counts = new LinkedHashMap<>();
            for (ReviewDto review : reviewDtos) {
                counts.put(review.score(), counts.get(review.score()) + 1);
            }

            List<ReviewCount> reviewCounts = counts.entrySet().stream()
                    .map(o -> new ReviewCount(o.getKey(), o.getValue()))
                    .toList();

            return new TotalReviewChart(reviewCounts);
        }

        private record ReviewCount(float point, int count) {
        }
    }

    record MonthlyReviewChart(
            List<MonthlyReviewCount> lineChartData
    ) {
        public static MonthlyReviewChart of(List<ReviewDto> reviewDtos) {

            EnumMap<Month, Object[]> counts = new EnumMap<>(Month.class);

            for (ReviewDto review : reviewDtos) {
                Object[] value = new Object[]{review.score(), 1};
                if (counts.containsKey(review.createdAt().getMonth())) {
                    float tempTotal = (float) counts.get(review.createdAt().getMonth())[0] + review.score();
                    int tempCount = (int) counts.get(review.createdAt().getMonth())[1] + 1;
                    value = new Object[]{tempTotal, tempCount};
                }
                counts.put(review.createdAt().getMonth(), value);
            }

            List<MonthlyReviewCount> monthlyReviewCounts = counts.entrySet().stream()
                    .map(o -> new MonthlyReviewCount(
                            o.getKey().getValue(),
                            (float) o.getValue()[0] / (int) o.getValue()[1],
                            (int) o.getValue()[1]))
                    .toList();

            return new MonthlyReviewChart(monthlyReviewCounts);
        }

        private record MonthlyReviewCount(int month, float avg, int count) {
        }
    }
}
