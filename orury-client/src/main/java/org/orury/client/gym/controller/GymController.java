package org.orury.client.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.gym.converter.message.GymMessage;
import org.orury.client.gym.converter.response.GymResponse;
import org.orury.client.gym.converter.response.GymReviewStatistics;
import org.orury.client.gym.converter.response.GymsResponse;
import org.orury.client.gym.service.GymLikeService;
import org.orury.client.gym.service.GymService;
import org.orury.client.review.service.ReviewService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.gym.dto.GymDto;
import org.orury.domain.review.dto.ReviewDto;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gyms")
@RestController
public class GymController {
    private final GymService gymService;
    private final GymLikeService gymLikeService;
    private final ReviewService reviewService;

    @Operation(summary = "암장 상세 조회", description = "gymId를 받아, 암장을 상세 정보를 돌려준다.")
    @GetMapping("/{id}")
    public ApiResponse getGymById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        GymDto gymDto = gymService.getGymDtoById(id);
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);
        boolean isLike = gymLikeService.isLiked(userPrincipal.id(), id);

        List<ReviewDto> reviewDtos = reviewService.getAllReviewDtosByGymId(id);
        GymReviewStatistics gymReviewStatistics = GymReviewStatistics.of(reviewDtos);

        GymResponse response = GymResponse.of(gymDto, doingBusiness, isLike, gymReviewStatistics);
        return ApiResponse.of(GymMessage.GYM_READ.getMessage(), response);
    }

    @Operation(summary = "암장 목록 검색", description = "검색어와 위치 좌표(경도, 위도)를 받아, 검색어를 포함하는 암장 목록을 가까운 순으로 돌려준다.")
    @GetMapping("/search")
    public ApiResponse getGymsByLocation(
            @RequestParam("search_word") String searchWord,
            @RequestParam float latitude,
            @RequestParam float longitude,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<GymDto> gymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc(searchWord, latitude, longitude);

        List<GymsResponse> response = gymDtos.stream()
                .map(gymDto -> {
                    boolean isLike = gymLikeService.isLiked(userPrincipal.id(), gymDto.id());
                    boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

                    return GymsResponse.of(gymDto, doingBusiness, isLike);
                })
                .toList();
        return ApiResponse.of(GymMessage.GYM_READ.getMessage(), response);
    }
}
