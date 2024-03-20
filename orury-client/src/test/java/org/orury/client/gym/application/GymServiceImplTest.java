package org.orury.client.gym.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ServiceTest;
import org.orury.common.error.code.GymErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.DomainFixtureFactory;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLikePK;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.orury.domain.DomainFixtureFactory.TestGym.createGym;
import static org.orury.domain.DomainFixtureFactory.TestGymDto.createGymDto;
import static org.orury.domain.DomainFixtureFactory.TestGymLikeDto.createGymLikeDto;

@DisplayName("[Service] 암장 ServiceImpl 테스트")
class GymServiceImplTest extends ServiceTest {

    @Test
    @DisplayName("존재하는 암장id가 들어오면, 정상적으로 GymDto를 반환한다.")
    void should_RetrieveGymDtoById() {
        // given
        Long gymId = 1L;
        Gym expectedGym = createGym().build().get();
        GymDto expectedGymDto = GymDto.from(expectedGym);

        given(gymReader.findGymById(any())).willReturn(Optional.of(expectedGym));

        // when
        GymDto actual = gymService.getGymDtoById(gymId);

        // then
        assertEquals(expectedGymDto, actual);

        then(gymReader).should(times(1))
                .findGymById(anyLong());
    }

    @Test
    @DisplayName("존재하지 않는 암장id가 들어오면, NOT_FOUND 예외를 반환한다.")
    void when_NotExistingGymId_Then_NotFoundException() {
        // given
        Long gymId = 2L;

        given(gymReader.findGymById(gymId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class,
                () -> gymService.getGymDtoById(gymId));

        then(gymReader).should(times(1))
                .findGymById(anyLong());
    }

    @Test
    @DisplayName("검색어, 경도, 위도에 대해, 해당 검색어가 포함된 List<Gym>을 가까운 순으로 정렬해서 반환한다.")
    void should_SortByDistanceAndRetrieveGymDtoList() {
        // given
        String searchWord = "anything";
        float currentLatitude = 10.111111f;
        float currentLongitude = 10.111111f;
        Gym middleGym = createGym()
                .id(1L)
                .latitude(20.000001)
                .longitude(50.543210).build().get();
        Gym mostFarGym = createGym()
                .id(2L)
                .latitude(50.123456)
                .longitude(137.654321).build().get();
        Gym mostNearGym = createGym()
                .id(3L)
                .latitude(10.111111)
                .longitude(10.111111).build().get();
        List<Gym> searchedGyms = List.of(middleGym, mostFarGym, mostNearGym);

        given(gymReader.findGymsBySearchWord(searchWord))
                .willReturn(searchedGyms);

        List<GymDto> expectedGymDtos = List.of(
                GymDto.from(mostNearGym),
                GymDto.from(middleGym),
                GymDto.from(mostFarGym)
        );

        // when
        List<GymDto> gymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc(searchWord, currentLatitude, currentLongitude);

        // then
        assertEquals(expectedGymDtos, gymDtos);
        then(gymReader).should(times(1))
                .findGymsBySearchWord(anyString());
    }

    @Test
    @DisplayName("검색어를 포함한 List<Gym>이 비어있어도, 빈 List를 반환한다.")
    void when_NothingSearched_Then_RetrieveEmptyList() {
        // given
        String searchWord = "anything";
        given(gymReader.findGymsBySearchWord(searchWord))
                .willReturn(List.of());
        List<GymDto> expectedGymDtos = Collections.emptyList();

        // when
        List<GymDto> actualGymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc(searchWord, 12.34f, 56.78f);

        // then
        assertEquals(expectedGymDtos, actualGymDtos);
        then(gymReader).should(times(1))
                .findGymsBySearchWord(anyString());
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 없다면, 정상적으로 암장 좋아요를 생성한다.")
    void should_CreateGymLike() {
        // given
        GymLikePK gymLikePK = GymLikePK.of(2L, 1L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK).build().get();

        given(gymReader.existsGymById(anyLong()))
                .willReturn(true);
        given(gymReader.existsGymLikeById(gymLikePK))
                .willReturn(false);

        // when
        gymService.createGymLike(gymLikeDto);

        // then
        then(gymReader).should(times(1))
                .existsGymById(anyLong());
        then(gymReader).should(times(1))
                .existsGymLikeById(any());
        then(gymStore).should(times(1))
                .createGymLike(any());
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 있다면, 생성하지 않고 return한다.")
    void when_AlreadyExistingGymLike_Then_ReturnWithoutSave() {
        // given
        GymLikePK gymLikePK = GymLikePK.of(1L, 2L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK).build().get();

        given(gymReader.existsGymById(anyLong()))
                .willReturn(true);
        given(gymReader.existsGymLikeById(gymLikePK))
                .willReturn(true);

        // when
        gymService.createGymLike(gymLikeDto);

        // then
        then(gymReader).should(times(1))
                .existsGymById(anyLong());
        then(gymReader).should(times(1))
                .existsGymLikeById(any());
        then(gymStore).should(never())
                .createGymLike(any());
    }

    @Test
    @DisplayName("존재하지 않는 암장에 대한 암장좋아요 생성 요청이 들어온다면, NotFound 예외를 반환한다.")
    void when_AttemptToCreateGymLikeForNotExistingGym_Then_NotFoundException() {
        // given
        Long gymId = 4L;
        GymLikePK gymLikePK = GymLikePK.of(3L, gymId);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK).build().get();

        given(gymReader.existsGymById(gymId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> gymService.createGymLike(gymLikeDto));

        assertEquals(GymErrorCode.NOT_FOUND.getMessage(), exception.getMessage());

        then(gymReader).should(times(1))
                .existsGymById(anyLong());
        then(gymReader).should(never())
                .existsGymLikeById(any());
        then(gymStore).should(never())
                .createGymLike(any());
    }

    @Test
    @DisplayName("존재하지 않는 암장에 대한 암장좋아요 삭제 요청이 들어온다면, NotFound 예외를 반환한다.")
    void when_AttemptToDeleteGymLikeForNotExistingGym_Then_NotFoundException() {
        // given
        Long gymId = 6L;
        GymLikePK gymLikePK = GymLikePK.of(77L, gymId);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK).build().get();

        given(gymReader.existsGymById(gymId))
                .willReturn(false);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> gymService.deleteGymLike(gymLikeDto));

        assertEquals(GymErrorCode.NOT_FOUND.getMessage(), exception.getMessage());

        then(gymReader).should(times(1))
                .existsGymById(anyLong());
        then(gymReader).should(never())
                .existsGymLikeById(any());
        then(gymStore).should(never())
                .deleteGymLike(any());
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 있다면, 정상적으로 암장 좋아요를 삭제한다.")
    void should_DeleteGymLike() {
        // given
        GymLikePK gymLikePK = GymLikePK.of(4L, 3L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK).build().get();

        given(gymReader.existsGymById(anyLong()))
                .willReturn(true);
        given(gymReader.existsGymLikeById(gymLikePK))
                .willReturn(true);

        // when
        gymService.deleteGymLike(gymLikeDto);

        // then
        then(gymReader).should(times(1))
                .existsGymById(anyLong());
        then(gymReader).should(times(1))
                .existsGymLikeById(any());
        then(gymStore).should(times(1))
                .deleteGymLike(any());
    }

    @Test
    @DisplayName("암장에 대한 유저의 암장 좋아요 기존에 없다면, 삭제하지 않고 return한다.")
    void when_NotExistingGymLike_Then_ReturnWithoutDelete() {
        // given
        GymLikePK gymLikePK = GymLikePK.of(3L, 4L);
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK).build().get();

        given(gymReader.existsGymById(anyLong()))
                .willReturn(true);
        given(gymReader.existsGymLikeById(gymLikePK))
                .willReturn(false);

        // when
        gymService.deleteGymLike(gymLikeDto);

        // then
        then(gymReader).should(times(1))
                .existsGymById(anyLong());
        then(gymReader).should(times(1))
                .existsGymLikeById(any());
        then(gymStore).should(never())
                .deleteGymLike(any());
    }

    @Test
    @DisplayName("유저id와 암장id에 대해 좋아요가 존재하면, true를 반환한다.")
    void when_IdOfExitingGymLike_Then_ReturnTrue() {
        // given
        Long userId = 1L;
        Long gymId = 2L;

        given(gymReader.existsGymLikeByUserIdAndGymId(userId, gymId))
                .willReturn(true);

        // when & then
        assertTrue(gymService.isLiked(userId, gymId));

        then(gymReader).should(times(1))
                .existsGymLikeByUserIdAndGymId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("유저id와 암장id에 대해 좋아요가 존재하지 않으면, false를 반환한다.")
    void when_IdOfNotExitingGymLike_Then_ReturnFalse() {
        //given
        Long userId = 1L;
        Long gymId = 2L;

        given(gymReader.existsGymLikeByUserIdAndGymId(userId, gymId))
                .willReturn(false);

        // when & then
        assertFalse(gymService.isLiked(userId, gymId));

        then(gymReader).should(times(1))
                .existsGymLikeByUserIdAndGymId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("현재 시간이 GymDto의 영업시간 사이에 있다면, true를 반환한다.")
    void when_CurrentTimeIsBetweenBusinessHours_Then_ReturnTrue() {
        // given
        GymDto gymDto = createGymDtoWithOpenCloseTime(
                LocalTime.now().minusMinutes(1L),
                LocalTime.now().plusMinutes(1L)
        ).build().get();

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertTrue(doingBusiness);
    }

    @Test
    @DisplayName("현재 시간이 GymDto의 영업시간 전이라면, false를 반환한다.")
    void when_CurrentTimeIsBeforeBusinessHours_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDtoWithOpenCloseTime(
                LocalTime.now().plusMinutes(1L),
                LocalTime.now().plusMinutes(2L)
        ).build().get();

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    @Test
    @DisplayName("현재 시간이 GymDto의 영업시간 후이라면, false를 반환한다.")
    void when_CurrentTimeIsAfterBusinessHours_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDtoWithOpenCloseTime(
                LocalTime.now().minusMinutes(2L),
                LocalTime.now().minusMinutes(1L)
        ).build().get();

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    @Test
    @DisplayName("GymDto의 현재 요일 영업시간이 비어있다면(null이라면), false를 반환한다.")
    void when_BusinessHoursIsNull_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDto(null).build().get();

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    @Test
    @DisplayName("GymDto의 현재 요일 영업시간이 올바르지 않은 형식으로 돼있어도, false를 반환한다.")
    void when_InvalidTypeOfBusinessHours_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDtoWithInvalidTime("12시30분~23시").build().get();

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    private DomainFixtureFactory.TestGymDto.TestGymDtoBuilder createGymDtoWithOpenCloseTime(LocalTime openTime, LocalTime closeTime) {
        EnumMap<DayOfWeek, String> businessHours = new EnumMap<>(DayOfWeek.class);
        businessHours.put(DayOfWeek.MONDAY, openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute());
        businessHours.put(DayOfWeek.TUESDAY, openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute());
        businessHours.put(DayOfWeek.WEDNESDAY, openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute());
        businessHours.put(DayOfWeek.THURSDAY, openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute());
        businessHours.put(DayOfWeek.FRIDAY, openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute());
        businessHours.put(DayOfWeek.SATURDAY, openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute());
        businessHours.put(DayOfWeek.SUNDAY, openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute());
        return DomainFixtureFactory.TestGymDto.createGymDto()
                .businessHours(businessHours);
    }

    private DomainFixtureFactory.TestGymDto.TestGymDtoBuilder createGymDtoWithInvalidTime(String businessHour) {
        EnumMap<DayOfWeek, String> businessHours = new EnumMap<>(DayOfWeek.class);
        businessHours.put(DayOfWeek.MONDAY, businessHour);
        businessHours.put(DayOfWeek.TUESDAY, businessHour);
        businessHours.put(DayOfWeek.WEDNESDAY, businessHour);
        businessHours.put(DayOfWeek.THURSDAY, businessHour);
        businessHours.put(DayOfWeek.FRIDAY, businessHour);
        businessHours.put(DayOfWeek.SATURDAY, businessHour);
        businessHours.put(DayOfWeek.SUNDAY, businessHour);
        return DomainFixtureFactory.TestGymDto.createGymDto()
                .businessHours(businessHours);
    }
}
