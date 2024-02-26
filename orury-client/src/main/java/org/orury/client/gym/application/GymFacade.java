package org.orury.client.gym.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.gym.converter.response.GymResponse;
import org.orury.client.gym.converter.response.GymReviewStatistics;
import org.orury.client.gym.converter.response.GymsResponse;
import org.orury.client.gym.service.GymService;
import org.orury.client.review.service.ReviewService;
import org.orury.domain.gym.db.model.GymLike;
import org.orury.domain.gym.db.model.GymLikePK;
import org.orury.domain.gym.dto.GymLikeDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private final GymService gymService;
    private final ReviewService reviewService;

    public GymResponse getGymById(Long gymId, Long userId) {
        var gymDto = gymService.getGymDtoById(gymId);
        var doingBusiness = gymService.checkDoingBusiness(gymDto);
        var isLike = gymService.isLiked(userId, gymId);

        var reviewDtos = reviewService.getAllReviewDtosByGymId(gymId);
        var gymReviewStatistics = GymReviewStatistics.of(reviewDtos);

        return GymResponse.of(gymDto, doingBusiness, isLike, gymReviewStatistics);
    }

    public List<GymsResponse> getGymsByLocation(String searchWord, float latitude, float longitude, Long userId) {
        var gymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc(searchWord, latitude, longitude);

        return gymDtos.stream()
                .map(gymDto -> {
                    boolean isLike = gymService.isLiked(userId, gymDto.id());
                    boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

                    return GymsResponse.of(gymDto, doingBusiness, isLike);
                })
                .toList();
    }

    public void createGymLike(Long gymId, Long userId) {
        gymService.isValidate(gymId);
        var gymLikeDto = GymLikeDto.from(GymLike.of(GymLikePK.of(userId, gymId)));
        gymService.createGymLike(gymLikeDto);
    }

    public void deleteGymLike(Long gymId, Long userId) {
        gymService.isValidate(gymId);
        var gymLikeDto = GymLikeDto.from(GymLike.of(GymLikePK.of(userId, gymId)));
        gymService.deleteGymLike(gymLikeDto);
    }
}
