package org.fastcampus.oruryclient.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.gym.converter.message.GymMessage;
import org.fastcampus.oruryclient.gym.converter.request.GymSearchRequest;
import org.fastcampus.oruryclient.gym.converter.response.GymDetailResponse;
import org.fastcampus.oruryclient.gym.converter.response.GymsResponse;
import org.fastcampus.oruryclient.gym.service.GymLikeService;
import org.fastcampus.oruryclient.gym.service.GymService;
import org.fastcampus.oruryclient.review.service.ReviewService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/gyms")
@RestController
public class GymController {
    private final GymService gymService;
    private final GymLikeService gymLikeService;
    private final ReviewService reviewService;

    @Operation(summary = "암장 상세 조회", description = "gymId를 받아, 암장을 상세 정보를 돌려준다.")
    @PostMapping("/{id}")
    public ApiResponse<GymDetailResponse> getGymById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        GymDto gymDto = gymService.getGymDtoById(id);
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);
        boolean isLike = gymLikeService.isLiked(userPrincipal.id(), id);
        List<ReviewDto> reviewDtos = reviewService.getAllReviewDtosByGymId(id);

        GymDetailResponse response = GymDetailResponse.of(gymDto, doingBusiness, isLike, reviewDtos);

        return ApiResponse.<GymDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message(GymMessage.GYM_READ.getMessage())
                .data(response)
                .build();
    }

    @Operation(summary = "암장 목록 검색", description = "검색어와 위치 좌표(경도, 위도)를 받아, 검색어를 포함하는 암장 목록을 가까운 순으로 돌려준다.")
    @PostMapping("/search")
    public ApiResponse<List<GymsResponse>> getGymsByLocation(@RequestBody GymSearchRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<GymDto> gymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc(request.searchWord(), request.latitude(), request.longitude());

        List<GymsResponse> response = gymDtos.stream()
                .map(gymDto -> {
                    boolean isLike = gymLikeService.isLiked(userPrincipal.id(), gymDto.id());
                    boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

                    return GymsResponse.of(gymDto, doingBusiness, isLike);
                }).toList();

        return ApiResponse.<List<GymsResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(GymMessage.GYM_READ.getMessage())
                .data(response)
                .build();
    }

}
