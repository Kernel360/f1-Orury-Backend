package org.orury.client.gym.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.FacadeTest;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("[Facade] 암장 Facade 테스트")
class GymFacadeTest extends FacadeTest {

    @DisplayName("성공적으로 (암장 영업중 유무, 유저의 암장좋아요 유무, 암장리뷰 통계를 포함한) GymReponse를 반환한다.")
    @Test
    void should_RetrieveGymResponse() {
        // given
        Long gymId = 1L;
        Long userId = 2L;
        GymDto gymDto = createGymDto(gymId);
        List<ReviewDto> reviewDtos = List.of(
                createReviewDto(1L, gymDto, userId),
                createReviewDto(2L, gymDto, userId),
                createReviewDto(3L, gymDto, userId)
        );

        given(gymService.getGymDtoById(gymId))
                .willReturn(gymDto);
        given(gymService.checkDoingBusiness(gymDto))
                .willReturn(true);
        given(gymService.isLiked(userId, gymId))
                .willReturn(false);
        given(reviewService.getAllReviewDtosByGymId(gymId))
                .willReturn(reviewDtos);

        // when
        gymFacade.getGymById(gymId, userId);

        // then
        then(gymService).should(times(1))
                .getGymDtoById(anyLong());
        then(gymService).should(times(1))
                .checkDoingBusiness(any());
        then(gymService).should(times(1))
                .isLiked(anyLong(), anyLong());
        then(reviewService).should(times(1))
                .getAllReviewDtosByGymId(anyLong());
    }

    @DisplayName("해당 암장에 달린 암장리뷰가 없어도 GymReponse를 정상적으로 반환한다.")
    @Test
    void when_GymDoesNotHaveAnyReview_Then_RetrieveGymResponse() {
        // given
        Long gymId = 1L;
        Long userId = 2L;
        GymDto gymDto = createGymDtoWithNoReview(gymId);
        List<ReviewDto> emptyReviewDtos = Collections.emptyList();

        given(gymService.getGymDtoById(gymId))
                .willReturn(gymDto);
        given(gymService.checkDoingBusiness(gymDto))
                .willReturn(true);
        given(gymService.isLiked(userId, gymId))
                .willReturn(false);
        given(reviewService.getAllReviewDtosByGymId(gymId))
                .willReturn(emptyReviewDtos);

        // when
        gymFacade.getGymById(gymId, userId);

        // then
        then(gymService).should(times(1))
                .getGymDtoById(anyLong());
        then(gymService).should(times(1))
                .checkDoingBusiness(any());
        then(gymService).should(times(1))
                .isLiked(anyLong(), anyLong());
        then(reviewService).should(times(1))
                .getAllReviewDtosByGymId(anyLong());
    }

    @DisplayName("성공적으로 (암장 영업중 유무, 유저의 암장좋아요 유무를 포함한) GymsReponses를 반환한다.")
    @Test
    void should_RetrieveGymsResponses() {
        // given
        String searchWord = "검색어";
        float latitude = 33.455245f;
        float longitude = 126.231323f;
        Long userId = 2L;
        List<GymDto> gymDtos = List.of(
                createGymDto(1L),
                createGymDtoWithNoReview(2L),
                createGymDto(3L)
        );

        given(gymService.getGymDtosBySearchWordOrderByDistanceAsc(searchWord, latitude, longitude))
                .willReturn(gymDtos);
        given(gymService.checkDoingBusiness(any()))
                .willReturn(true, false, true);
        given(gymService.isLiked(anyLong(), anyLong()))
                .willReturn(false, true, false);

        // when
        gymFacade.getGymsBySearchWordAndLocation(searchWord, latitude, longitude, userId);

        // then
        then(gymService).should(times(1))
                .getGymDtosBySearchWordOrderByDistanceAsc(anyString(), anyFloat(), anyFloat());
        then(gymService).should(times(gymDtos.size()))
                .checkDoingBusiness(any());
        then(gymService).should(times(gymDtos.size()))
                .isLiked(anyLong(), anyLong());
    }

    @DisplayName("성공적으로 암장좋아요를 생성한다.")
    @Test
    void should_CreateGymLike() {
        // given
        Long userId = 3L;
        Long gymId = 4L;
        GymLikeDto gymLikeDto = createGymLikeDto(userId, gymId);

        // when
        gymFacade.createGymLike(gymLikeDto);

        // then
        then(gymService).should(times(1))
                .createGymLike(any());
    }

    @DisplayName("성공적으로 암장좋아요를 삭제한다.")
    @Test
    void should_DeleteGymLike() {
        // given
        Long userId = 3L;
        Long gymId = 4L;
        GymLikeDto gymLikeDto = createGymLikeDto(userId, gymId);

        // when
        gymFacade.deleteGymLike(gymLikeDto);

        // then
        then(gymService).should(times(1))
                .deleteGymLike(any());
    }

    private GymDto createGymDto(Long gymId) {
        return GymDto.of(
                gymId,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                4.5f,
                12,
                11,
                List.of("image1"),
                37.513709,
                127.062144,
                "더클라임",
                "01012345678",
                "gymInstagramLink.com",
                "MONDAY",
                LocalDateTime.of(1999, 3, 1, 7, 30),
                LocalDateTime.of(2024, 1, 23, 18, 32),
                "11:11-23:11",
                "11:22-23:22",
                "11:33-23:33",
                "11:44-23:44",
                "11:55-23:55",
                "11:66-23:66",
                "11:77-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private GymDto createGymDtoWithNoReview(Long gymId) {
        return GymDto.of(
                gymId,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                4.5f,
                0,
                11,
                Collections.emptyList(),
                37.513709,
                127.062144,
                "더클라임",
                "01012345678",
                "gymInstagramLink.com",
                "MONDAY",
                LocalDateTime.of(1999, 3, 1, 7, 30),
                LocalDateTime.of(2024, 1, 23, 18, 32),
                "11:11-23:11",
                "11:22-23:22",
                "11:33-23:33",
                "11:44-23:44",
                "11:55-23:55",
                "11:66-23:66",
                "11:77-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private ReviewDto createReviewDto(Long id, GymDto gymDto, Long userId) {
        return ReviewDto.of(
                id,
                "reviewContent",
                List.of(),
                4.5f,
                0,
                1,
                2,
                3,
                4,
                UserDto.from(createUser(userId)),
                gymDto,
                LocalDateTime.of(2024, 1, 1, 14, 23),
                LocalDateTime.of(2024, 1, 25, 4, 56)
        );
    }

    private User createUser(Long id) {
        return User.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                null,
                "userProfileImage",
                null,
                null,
                UserStatus.ENABLE
        );
    }

    private GymLikeDto createGymLikeDto(Long userId, Long gymId) {
        return GymLikeDto.of(GymLikePK.of(userId, gymId));
    }
}
