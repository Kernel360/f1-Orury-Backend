package org.orury.client.gym.service;

import org.orury.domain.gym.db.model.GymLike;
import org.orury.domain.gym.db.model.GymLikePK;
import org.orury.domain.gym.db.repository.GymLikeRepository;
import org.orury.domain.gym.db.repository.GymRepository;
import org.orury.domain.gym.dto.GymLikeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 암장 좋아요 테스트")
@ActiveProfiles("test")
class GymLikeServiceTest {

    private GymLikeService gymLikeService;
    private GymLikeRepository gymLikeRepository;
    private GymRepository gymRepository;

    @BeforeEach
    void setUp() {
        gymLikeRepository = mock(GymLikeRepository.class);
        gymRepository = mock(GymRepository.class);
        gymLikeService = new GymLikeService(gymLikeRepository, gymRepository);
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 없다면, 정상적으로 암장 좋아요를 생성한다.")
    void should_CreateGymLike() {
        //given
        GymLikePK gymLikePK = GymLikePK.of(2L, 1L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

        given(gymLikeRepository.existsById(gymLikePK))
                .willReturn(false);

        // when
        gymLikeService.createGymLike(gymLikeDto);

        //then
        then(gymLikeRepository).should()
                .existsById(any());
        then(gymLikeRepository).should()
                .save(any());
        then(gymRepository).should()
                .increaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 있다면, 생성하지 않고 return한다.")
    void when_AlreadyExistingGymLike_Then_ReturnWithoutSave() {
        //given
        GymLikePK gymLikePK = GymLikePK.of(1L, 2L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

        given(gymLikeRepository.existsById(gymLikePK))
                .willReturn(true);

        // when
        gymLikeService.createGymLike(gymLikeDto);

        //then
        then(gymLikeRepository).should().
                existsById(any());
        then(gymLikeRepository).should(never())
                .save(any());
        then(gymRepository).should(never())
                .increaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 있다면, 정상적으로 암장 좋아요를 삭제한다.")
    void should_DeleteGymLike() {
        //given
        GymLikePK gymLikePK = GymLikePK.of(4L, 3L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

        given(gymLikeRepository.existsById(gymLikePK))
                .willReturn(true);

        // when
        gymLikeService.deleteGymLike(gymLikeDto);

        //then
        then(gymLikeRepository).should()
                .existsById(any());
        then(gymLikeRepository).should()
                .delete(any());
        then(gymRepository).should()
                .decreaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 없다면, 삭제하지 않고 return한다.")
    void when_NotExistingGymLike_Then_ReturnWithoutDelete() {
        //given
        GymLikePK gymLikePK = GymLikePK.of(3L, 4L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

        given(gymLikeRepository.existsById(gymLikePK))
                .willReturn(false);

        // when
        gymLikeService.deleteGymLike(gymLikeDto);

        //then
        then(gymLikeRepository).should()
                .existsById(any());
        then(gymLikeRepository).should(never())
                .delete(any());
        then(gymRepository).should(never())
                .decreaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("유저id와 암장id에 대해 좋아요가 존재하면, true를 반환한다.")
    void when_idsOfExitingGymLike_Then_ReturnTrue() {
        //given
        Long userId = 1L;
        Long gymId = 2L;

        given(gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(1L, 2L))
                .willReturn(true);

        boolean expectedValue = true;

        // when
        boolean isLiked = gymLikeService.isLiked(userId, gymId);

        //then
        assertEquals(expectedValue, isLiked);
        then(gymLikeRepository).should()
                .existsByGymLikePK_UserIdAndGymLikePK_GymId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("유저id와 암장id에 대해 좋아요가 존재하지 않으면, false를 반환한다.")
    void when_idsOfNotExitingGymLike_Then_ReturnFalse() {
        //given
        Long userId = 1L;
        Long gymId = 2L;

        given(gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(1L, 2L))
                .willReturn(false);

        boolean expectedValue = false;

        // when
        boolean isLiked = gymLikeService.isLiked(userId, gymId);

        //then
        assertEquals(expectedValue, isLiked);
        then(gymLikeRepository).should()
                .existsByGymLikePK_UserIdAndGymLikePK_GymId(anyLong(), anyLong());
    }

    private static GymLikeDto createGymLikeDto(GymLikePK gymLikePK) {
        return GymLikeDto.from(GymLike.of(gymLikePK));
    }
}
