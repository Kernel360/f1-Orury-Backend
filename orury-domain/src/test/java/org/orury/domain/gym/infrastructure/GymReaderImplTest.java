package org.orury.domain.gym.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLikePK;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("[Reader] 암장 ReaderImpl 테스트")
class GymReaderImplTest extends InfrastructureTest {

    @Test
    @DisplayName("존재하는 암장id가 들어오면, 정상적으로 Gym Entity를 반환한다.")
    void should_RetrieveGymById() {
        // given
        Long gymId = 3L;
        Gym gym = createGym(gymId);

        given(gymRepository.findById(gymId))
                .willReturn(Optional.of(gym));

        // when
        gymReader.findGymById(gymId);

        // then
        then(gymRepository).should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 암장id가 들어와도, (NotFound 예외를 발생시키지 않고) Option.empty()를 반환한다.")
    void when_NotExistingGymId_Then_NotFoundException() {
        // given
        Long gymId = 4L;

        given(gymRepository.findById(gymId))
                .willReturn(Optional.empty());

        // when & then
        gymReader.findGymById(gymId);

        then(gymRepository).should(times(1))
                .findById(anyLong());
    }

    @Test
    @DisplayName("존재하는 암장id가 들어오면, true를 반환한다.")
    void when_ExistingGymId_Then_ReturnTrue() {
        // given
        Long gymId = 7L;

        given(gymRepository.existsById(gymId))
                .willReturn(true);

        // when & then
        assertTrue(gymReader.existsGymById(gymId));
    }

    @Test
    @DisplayName("존재하지 않는 암장id가 들어오면, true를 반환한다.")
    void when_NotExistingGymId_Then_ReturnFalse() {
        Long gymId = 8L;

        given(gymRepository.existsById(gymId))
                .willReturn(false);

        // when & then
        assertFalse(gymReader.existsGymById(gymId));
    }

    @Test
    @DisplayName("암장명에 검색어를 포함하는 암장 리스트를 반환한다.")
    void when_AnyGymContainsSearchWordInTitle_Then_ReturnGymList() {
        // given
        String searchWord = "더클라임";
        List<Gym> gyms = List.of(createGym(1L), createGym(2L));

        given(gymRepository.findByNameContainingOrAddressContainingOrRoadAddressContaining(searchWord, searchWord, searchWord))
                .willReturn(gyms);

        // when
        gymReader.findGymsBySearchWord(searchWord);

        // then
        then(gymRepository).should(times(1))
                .findByNameContainingOrAddressContainingOrRoadAddressContaining(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("암장명에 검색어를 포함하는 암장이 없다면 빈 리스트를 반환한다.")
    void when_EveryGymDoesNotContainsSearchWordInTitle_Then_EmptyList() {
        // given
        String searchWord = "축구공";
        List<Gym> emptyList = Collections.emptyList();

        given(gymRepository.findByNameContainingOrAddressContainingOrRoadAddressContaining(searchWord, searchWord, searchWord))
                .willReturn(emptyList);

        // when
        gymReader.findGymsBySearchWord(searchWord);

        // then
        then(gymRepository).should(times(1))
                .findByNameContainingOrAddressContainingOrRoadAddressContaining(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("존재하는 (userId와 gymId로 구성된) 암장좋아요PK가 들어오면, true를 반환한다.")
    void when_ExistingGymLikePK_Then_ReturnTrue() {
        // given
        GymLikePK gymLikePK = createGymLikePK();

        given(gymLikeRepository.existsById(gymLikePK))
                .willReturn(true);

        // when & then
        assertTrue(gymReader.existsGymLikeById(gymLikePK));
    }

    @Test
    @DisplayName("존재하지 않는 (userId와 gymId로 구성된) 암장좋아요PK가 들어오면, false를 반환한다.")
    void when_NotExistingGymLikePK_Then_ReturnFalse() {
        // given
        GymLikePK gymLikePK = createGymLikePK();

        given(gymLikeRepository.existsById(gymLikePK))
                .willReturn(false);

        // when & then
        assertFalse(gymReader.existsGymLikeById(gymLikePK));
    }

    @Test
    @DisplayName("userId와 gymId로 구성된 암장좋아요가 존재하면, true를 반환한다.")
    void when_ExistingGymLikeConsistOfUserIdAndGymId_Then_ReturnTrue() {
        Long userId = 2L;
        Long gymId = 5L;

        given(gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(userId, gymId))
                .willReturn(true);

        // when & then
        assertTrue(gymReader.existsGymLikeByUserIdAndGymId(userId, gymId));
    }

    @Test
    @DisplayName("userId와 gymId로 구성된 암장좋아요가 존재하지 않으면, false를 반환한다.")
    void when_NotExistingGymLikeConsistOfUserIdAndGymId_Then_ReturnFalse() {
        // given
        Long userId = 2L;
        Long gymId = 5L;

        given(gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(userId, gymId))
                .willReturn(false);

        // when & then
        assertFalse(gymReader.existsGymLikeByUserIdAndGymId(userId, gymId));
    }

    private Gym createGym(Long id) {
        return Gym.of(
                id,
                "gymName",
                "gymKakaoId",
                "gymRoadAddress",
                "gymAddress",
                40.5f,
                23,
                12,
                List.of(),
                123.456,
                123.456,
                "gymBrand",
                "010-1234-5678",
                "gymInstaLink",
                "MONDAY",
                "11:00-23:11",
                "12:00-23:22",
                "13:00-23:33",
                "14:00-23:44",
                "15:00-23:55",
                "16:00-23:66",
                "17:00-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private GymLikePK createGymLikePK() {
        return GymLikePK.of(1L, 1L);
    }
}
