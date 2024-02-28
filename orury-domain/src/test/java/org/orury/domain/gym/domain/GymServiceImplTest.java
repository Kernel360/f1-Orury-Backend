package org.orury.domain.gym.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.GymErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLike;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.orury.common.util.S3Folder.GYM;

@ExtendWith(MockitoExtension.class)
@DisplayName("[GymServiceImpl] 암장 ServiceImpl 테스트")
@ActiveProfiles("test")
class GymServiceImplTest {

    GymService gymService;
    GymReader gymReader;
    GymStore gymStore;
    ImageReader imageReader;

    @BeforeEach
    void setUp() {
        gymReader = mock(GymReader.class);
        gymStore = mock(GymStore.class);
        imageReader = mock(ImageReader.class);

        gymService = new GymServiceImpl(gymReader, gymStore, imageReader);
    }

    @Test
    @DisplayName("존재하는 암장id가 들어오면, 정상적으로 GymDto를 반환한다.")
    void should_RetrieveGymDtoById() {
        // given
        Long gymId = 1L;
        Gym gym = createGym(gymId);
        List<String> images = List.of("image1", "image2", "image3");

        given(gymReader.findGymById(gymId))
                .willReturn(gym);
        given(imageReader.getImageLinks(GYM, gym.getImages()))
                .willReturn(images);

        GymDto expectedGymDto = GymDto.from(gym, images);

        // when
        GymDto actualGymDto = gymService.getGymDtoById(gymId);

        // then
        assertEquals(expectedGymDto, actualGymDto);

        then(gymReader).should(times(1))
                .findGymById(anyLong());
        then(imageReader).should(times(1))
                .getImageLinks(any(), any());
    }

    @Test
    @DisplayName("존재하지 않는 암장id가 들어오면, NOT_FOUND 예외를 반환한다.")
    void when_NotExistingGymId_Then_NotFoundException() {
        // given
        Long gymId = 2L;

        given(gymReader.findGymById(gymId))
                .willThrow(new BusinessException(GymErrorCode.NOT_FOUND));

        // when & then
        assertThrows(BusinessException.class,
                () -> gymService.getGymDtoById(gymId));

        then(gymReader).should(times(1))
                .findGymById(anyLong());
        then(imageReader).should(never())
                .getImageLinks(any(), any());
    }

    @Test
    @DisplayName("검색어, 경도, 위도에 대해, 해당 검색어가 포함된 List<Gym>을 가까운 순으로 정렬해서 반환한다.")
    void should_SortByDistanceAndRetrieveGymDtoList() {
        // given
        String searchWord = "anything";
        float currentLatitude = 10.111111f;
        float currentLongitude = 10.111111f;
        Gym middleGym = createGym(
                1L,
                "20.000001",
                "50.543210"
        );
        Gym mostFarGym = createGym(
                2L,
                "50.123456",
                "137.654321"
        );
        Gym mostNearGym = createGym(
                3L,
                "11.110111",
                "12.222222"
        );
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
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

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
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

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
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

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
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

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
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

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
        GymLikeDto gymLikeDto = createGymLikeDto(gymLikePK);

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
    void when_idsOfExitingGymLike_Then_ReturnTrue() {
        // given
        Long userId = 1L;
        Long gymId = 2L;

        given(gymReader.existsGymLikeByUserIdAndGymId(userId, gymId))
                .willReturn(true);

        boolean expectedValue = true;

        // when
        boolean isLiked = gymService.isLiked(userId, gymId);

        // then
        assertEquals(expectedValue, isLiked);

        then(gymReader).should(times(1))
                .existsGymLikeByUserIdAndGymId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("유저id와 암장id에 대해 좋아요가 존재하지 않으면, false를 반환한다.")
    void when_idsOfNotExitingGymLike_Then_ReturnFalse() {
        //given
        Long userId = 1L;
        Long gymId = 2L;

        given(gymReader.existsGymLikeByUserIdAndGymId(userId, gymId))
                .willReturn(false);

        boolean expectedValue = false;

        // when
        boolean isLiked = gymService.isLiked(userId, gymId);

        //then
        assertEquals(expectedValue, isLiked);

        then(gymReader).should(times(1))
                .existsGymLikeByUserIdAndGymId(anyLong(), anyLong());
    }

    @Test
    @DisplayName("현재 시간이 GymDto의 영업시간 사이에 있다면, true를 반환한다.")
    void when_CurrentTimeIsBetweenBusinessHours_Then_ReturnTrue() {
        // given
        GymDto gymDto = createGymDto(
                LocalTime.now().minusMinutes(1L),
                LocalTime.now().plusMinutes(1L)
        );

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertTrue(doingBusiness);
    }

    @Test
    @DisplayName("현재 시간이 GymDto의 영업시간 전이라면, false를 반환한다.")
    void when_CurrentTimeIsBeforeBusinessHours_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDto(
                LocalTime.now()
                        .plusMinutes(1L),
                LocalTime.now()
                        .plusMinutes(2L)
        );

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    @Test
    @DisplayName("현재 시간이 GymDto의 영업시간 후이라면, false를 반환한다.")
    void when_CurrentTimeIsAfterBusinessHours_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDto(
                LocalTime.now().minusMinutes(2L),
                LocalTime.now().minusMinutes(1L)
        );

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    @Test
    @DisplayName("GymDto의 현재 요일 영업시간이 비어있다면(null이라면), false를 반환한다.")
    void when_BusinessHoursIsNull_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDto(null);

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    @Test
    @DisplayName("GymDto의 현재 요일 영업시간이 올바르지 않은 형식으로 돼있어도, false를 반환한다.")
    void when_InvalidTypeOfBusinessHours_Then_ReturnFalse() {
        // given
        GymDto gymDto = createGymDto("12시30분~23시");

        // when
        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);

        // then
        assertFalse(doingBusiness);
    }

    private static Gym createGym(Long id) {
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
                "123.456",
                "123.456",
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

    private static Gym createGym(Long id, String latitude, String longitude) {
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
                latitude,
                longitude,
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

    private static GymDto createGymDto(LocalTime openTime, LocalTime closeTime) {
        return GymDto.of(
                1L,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                40.5f,
                23,
                12,
                List.of(),
                "37.513709",
                "127.062144",
                "더클라임",
                "01012345678",
                "gymInstagramLink.com",
                "MONDAY",
                LocalDateTime.of(1999, 3, 1, 7, 30),
                LocalDateTime.of(2024, 1, 23, 18, 32),
                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private static GymDto createGymDto(String businessHour) {
        return GymDto.of(
                1L,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                40.5f,
                23,
                12,
                List.of(),
                "37.513709",
                "127.062144",
                "더클라임",
                "01012345678",
                "gymInstagramLink.com",
                "MONDAY",
                LocalDateTime.of(1999, 3, 1, 7, 30),
                LocalDateTime.of(2024, 1, 23, 18, 32),
                businessHour,
                businessHour,
                businessHour,
                businessHour,
                businessHour,
                businessHour,
                businessHour,
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private static GymLikeDto createGymLikeDto(GymLikePK gymLikePK) {
        return GymLikeDto.from(GymLike.of(gymLikePK));
    }
}
