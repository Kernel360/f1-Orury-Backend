package org.fastcampus.oruryclient.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.gym.converter.message.GymMessage;
import org.fastcampus.oruryclient.gym.service.GymLikeService;
import org.fastcampus.oruryclient.gym.service.GymService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.gym.db.model.GymLike;
import org.fastcampus.orurydomain.gym.db.model.GymLikePK;
import org.fastcampus.orurydomain.gym.dto.GymLikeDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gyms/like")
@RestController
public class GymLikeController {
    private final GymLikeService gymLikeService;
    private final GymService gymService;

    @Operation(summary = "암장 좋아요 생성", description = "암장 id를 받아, 암장 좋아요를 생성한다.")
    @PostMapping("/{gymId}")
    public ApiResponse createGymLike(@PathVariable Long gymId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        gymService.isValidate(gymId);
        GymLikeDto gymLikeDto = GymLikeDto.from(GymLike.of(GymLikePK.of(userPrincipal.id(), gymId)));

        gymLikeService.createGymLike(gymLikeDto);

        return ApiResponse.of(GymMessage.GYM_LIKE_CREATED.getMessage());
    }

    @Operation(summary = "암장 좋아요 삭제", description = "암장 id를 받아, 암장 좋아요를 삭제한다.")
    @DeleteMapping("/{gymId}")
    public ApiResponse deleteGymLike(@PathVariable Long gymId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        gymService.isValidate(gymId);
        GymLikeDto gymLikeDto = GymLikeDto.from(GymLike.of(GymLikePK.of(userPrincipal.id(), gymId)));

        gymLikeService.deleteGymLike(gymLikeDto);

        return ApiResponse.of(GymMessage.GYM_LIKE_DELETED.getMessage());
    }
}
