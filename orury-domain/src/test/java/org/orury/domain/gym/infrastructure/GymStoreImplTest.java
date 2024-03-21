package org.orury.domain.gym.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.gym.domain.entity.GymLike;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.orury.domain.GymDomainFixture.TestGymLike.createGymLike;

@DisplayName("[Store] 암장 StoreImpl 테스트")
class GymStoreImplTest extends InfrastructureTest {

    @Test
    @DisplayName("성공적으로 Gym의 reviewCount를 1만큼 늘리고, totalScore를 reviewScore만큼 더해야 한다.")
    void should_IncreaseReviewCountAndIncreaseTotalScore() {
        // given
        Long gymId = 1L;
        float reviewScore = 4.5f;

        // when
        gymStore.increaseReviewCountAndTotalScore(gymId, reviewScore);

        // then
        then(gymRepository).should(times(1))
                .increaseReviewCount(anyLong());
        then(gymRepository).should(times(1))
                .addTotalScore(anyLong(), anyFloat());
    }

    @Test
    @DisplayName("성공적으로 Gym의 totalScore를 oldScore만큼 빼고 newScore만큼 더해야 한다.")
    void should_UpdateTotalScore() {
        // given
        Long gymId = 2L;
        float oldScore = 3.5f;
        float newScore = 1f;

        // when
        gymStore.updateTotalScore(gymId, oldScore, newScore);

        // then
        then(gymRepository).should(times(1))
                .addTotalScore(anyLong(), anyFloat());
    }

    @Test
    @DisplayName("성공적으로 Gym의 reviewCount를 1만큼 줄이고, totalScore를 reviewScore만큼 빼야 한다.")
    void should_DecreaseReviewCountAndDecreaseTotalScore() {
        // given
        Long gymId = 3L;
        float reviewScore = 3.5f;

        // when
        gymStore.decreaseReviewCountAndTotalScore(gymId, reviewScore);

        // then
        then(gymRepository).should(times(1))
                .decreaseReviewCount(anyLong());
        then(gymRepository).should(times(1))
                .subtractTotalScore(anyLong(), anyFloat());
    }

    @Test
    @DisplayName("성공적으로 GymLike를 생성하고 Gym의 likeCount를 1만큼 늘려야 한다.")
    void should_CreateGymLikeAndIncreaseGymLIkeCount() {
        // given
        GymLike gymLike = createGymLike().build().get();

        // when
        gymStore.createGymLike(gymLike);

        // then
        then(gymLikeRepository).should(times(1))
                .save(any());
        then(gymRepository).should(times(1))
                .increaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("성공적으로 GymLike를 삭제하고 Gym의 likeCount를 1만큼 줄여야 한다.")
    void should_DeleteGymLikeAndDecreaseGymLIkeCount() {
        // given
        GymLike gymLike = createGymLike().build().get();

        // when
        gymStore.deleteGymLike(gymLike);

        // then
        then(gymLikeRepository).should(times(1))
                .delete(any());
        then(gymRepository).should(times(1))
                .decreaseLikeCount(anyLong());
    }

    @Test
    @DisplayName("성공적으로 GymLike를 삭제하고 Gym의 likeCount를 1만큼 줄여야 한다.")
    void when_GymLikeByUserIdExists_Then_DeleteGymLikeAndDecreaseLikeCount() {
        // given
        Long userId = 5L;
        List<GymLike> gymLikes = List.of(
                createGymLike(1L, userId).build().get(),
                createGymLike(2L, userId).build().get(),
                createGymLike(3L, userId).build().get(),
                createGymLike(4L, userId).build().get());

        given(gymLikeRepository.findByGymLikePK_UserId(userId))
                .willReturn(gymLikes);

        // when
        gymStore.deleteGymLikesByUserId(userId);

        // then
        then(gymLikeRepository).should(times(1))
                .findByGymLikePK_UserId(anyLong());
        then(gymRepository).should(times(gymLikes.size()))
                .decreaseLikeCount(anyLong());
        then(gymLikeRepository).should(times(gymLikes.size()))
                .delete(any());
    }
}
