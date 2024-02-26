package org.orury.client.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.gym.application.GymFacade;
import org.orury.client.gym.converter.message.GymMessage;
import org.orury.client.review.service.ReviewService;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gyms")
@RestController
public class GymController {
    private final GymFacade gymFacade;
    private final ReviewService reviewService;

    @Operation(summary = "암장 상세 조회", description = "gymId를 받아, 암장을 상세 정보를 돌려준다.")
    @GetMapping("/{id}")
    public ApiResponse getGymById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        var gymResponse = gymFacade.getGymById(id, userPrincipal.id());
        return ApiResponse.of(GymMessage.GYM_READ.getMessage(), gymResponse);
    }

    @Operation(summary = "암장 목록 검색", description = "검색어와 위치 좌표(경도, 위도)를 받아, 검색어를 포함하는 암장 목록을 가까운 순으로 돌려준다.")
    @GetMapping("/search")
    public ApiResponse getGymsByLocation(
            @RequestParam("search_word") String searchWord,
            @RequestParam float latitude,
            @RequestParam float longitude,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        var gymsResponse = gymFacade.getGymsByLocation(searchWord, latitude, longitude, userPrincipal.id());
        return ApiResponse.of(GymMessage.GYM_READ.getMessage(), gymsResponse);
    }

    @Operation(summary = "암장 좋아요 생성", description = "암장 id를 받아, 암장 좋아요를 생성한다.")
    @PostMapping("/like/{gymId}")
    public ApiResponse createGymLike(@PathVariable Long gymId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        gymFacade.createGymLike(gymId, userPrincipal.id());
        return ApiResponse.of(GymMessage.GYM_LIKE_CREATED.getMessage());
    }

    @Operation(summary = "암장 좋아요 삭제", description = "암장 id를 받아, 암장 좋아요를 삭제한다.")
    @DeleteMapping("/like/{gymId}")
    public ApiResponse deleteGymLike(@PathVariable Long gymId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        gymFacade.deleteGymLike(gymId, userPrincipal.id());
        return ApiResponse.of(GymMessage.GYM_LIKE_DELETED.getMessage());
    }
}
