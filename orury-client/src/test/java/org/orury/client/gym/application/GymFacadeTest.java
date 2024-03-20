package org.orury.client.gym.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.FacadeTest;
import org.orury.domain.DomainFixtureFactory;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;
import org.orury.domain.review.domain.dto.ReviewDto;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.orury.domain.DomainFixtureFactory.TestGymDto.createGymDto;
import static org.orury.domain.DomainFixtureFactory.TestGymLikeDto.createGymLikeDto;
import static org.orury.domain.DomainFixtureFactory.TestUserDto.createUserDto;

@DisplayName("[Facade] 암장 Facade 테스트")
class GymFacadeTest extends FacadeTest {

    @DisplayName("성공적으로 (암장 영업중 유무, 유저의 암장좋아요 유무, 암장리뷰 통계를 포함한) GymReponse를 반환한다.")
    @Test
    void should_RetrieveGymResponse() {
        // given
        Long gymId = 1L;
        Long userId = 2L;
        GymDto gymDto = createGymDto(gymId).build().get();
        List<ReviewDto> reviewDtos = List.of(
                createReviewDto(1L, gymDto, userId).build().get(),
                createReviewDto(2L, gymDto, userId).build().get(),
                createReviewDto(3L, gymDto, userId).build().get()
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
        GymDto gymDto = createGymDto(gymId)
                .reviewCount(0).build().get();
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
                createGymDto(1L).build().get(),
                createGymDto(2L).reviewCount(0).build().get(),
                createGymDto(3L).build().get()
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
        GymLikeDto gymLikeDto = createGymLikeDto(gymId, userId).build().get();

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
        GymLikeDto gymLikeDto = createGymLikeDto(gymId, userId).build().get();

        // when
        gymFacade.deleteGymLike(gymLikeDto);

        // then
        then(gymService).should(times(1))
                .deleteGymLike(any());
    }


    private DomainFixtureFactory.TestReviewDto.TestReviewDtoBuilder createReviewDto(Long id, GymDto gymDto, Long userId) {
        return DomainFixtureFactory.TestReviewDto.createReviewDto()
                .id(id)
                .gymDto(gymDto)
                .userDto(createUserDto(userId).build().get());
    }
}
