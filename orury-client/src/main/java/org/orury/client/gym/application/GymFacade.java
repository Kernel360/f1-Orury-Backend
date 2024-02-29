package org.orury.client.gym.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.gym.interfaces.response.GymResponse;
import org.orury.client.gym.interfaces.response.GymReviewStatistics;
import org.orury.client.gym.interfaces.response.GymsResponse;
import org.orury.client.review.service.ReviewService;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GymFacade {
    private final GymService gymService;
    private final ReviewService reviewService;

    public GymResponse getGymById(Long gymId, Long userId) {
        var gymDto = gymService.getGymDtoById(gymId);
        return convertGymDtoToGymResponse(gymDto, userId);
    }

    public List<GymsResponse> getGymsBySearchWordAndLocation(String searchWord, float latitude, float longitude, Long userId) {
        var gymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc(searchWord, latitude, longitude);
        return convertGymDtosToGymsResponses(gymDtos, userId);
    }

    public void createGymLike(GymLikeDto gymLikeDto) {
        gymService.createGymLike(gymLikeDto);
    }

    public void deleteGymLike(GymLikeDto gymLikeDto) {
        gymService.deleteGymLike(gymLikeDto);
    }

    private GymResponse convertGymDtoToGymResponse(GymDto gymDto, Long userId) {
        var doingBusiness = gymService.checkDoingBusiness(gymDto);
        var isLike = gymService.isLiked(userId, gymDto.id());
        var reviewDtos = reviewService.getAllReviewDtosByGymId(gymDto.id());
        var gymReviewStatistics = GymReviewStatistics.of(reviewDtos);

        return GymResponse.of(gymDto, doingBusiness, isLike, gymReviewStatistics);
    }

    private List<GymsResponse> convertGymDtosToGymsResponses(List<GymDto> gymDtos, Long userId) {
        return gymDtos.stream()
                .map(gymDto -> {
                    var isLike = gymService.isLiked(userId, gymDto.id());
                    var doingBusiness = gymService.checkDoingBusiness(gymDto);

                    return GymsResponse.of(gymDto, doingBusiness, isLike);
                })
                .toList();
    }
}
