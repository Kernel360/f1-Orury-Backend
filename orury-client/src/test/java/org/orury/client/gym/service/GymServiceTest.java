//package org.orury.client.gym.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.orury.common.error.code.GymErrorCode;
//import org.orury.common.error.exception.BusinessException;
//import org.orury.domain.global.domain.ImageUtils;
//import org.orury.client.gym.application.GymService;
//import org.orury.domain.gym.domain.dto.GymDto;
//import org.orury.domain.gym.domain.entity.Gym;
//import org.orury.domain.gym.infrastructure.GymRepository;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("[Service] 암장 테스트")
//@ActiveProfiles("test")
//class GymServiceTest {
//
//    private GymService gymService;
//    private GymRepository gymRepository;
//    private ImageUtils imageUtils;
//
//    @BeforeEach
//    void setUp() {
//        gymRepository = mock(GymRepository.class);
//        imageUtils = mock(ImageUtils.class);
//        gymService = new GymService(gymRepository, imageUtils);
//    }
//
//    @Test
//    @DisplayName("존재하는 암장id가 들어오면, 정상적으로 GymDto를 반환한다.")
//    void should_RetrieveGymDtoById() {
//        //given
//        Long gymId = 1L;
//        Gym gym = createGym(gymId);
//
//        given(gymRepository.findById(gymId))
//                .willReturn(Optional.of(gym));
//
//        GymDto expectedGymDto = GymDto.from(gym);
//
//        //when
//        GymDto gymDto = gymService.getGymDtoById(gymId);
//
//        //then
//        assertEquals(expectedGymDto, gymDto);
//        then(gymRepository).should()
//                .findById(anyLong());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 암장id가 들어오면, NOT_FOUND 예외를 반환한다.")
//    void two() {
//        //given
//        Long gymId = 1L;
//        Gym gym = createGym(gymId);
//
//        given(gymRepository.findById(gymId))
//                .willReturn(Optional.empty());
//
//        GymDto expectedGymDto = GymDto.from(gym);
//
//        //when & then
//        BusinessException exception = assertThrows(BusinessException.class,
//                () -> gymService.getGymDtoById(gymId));
//
//        assertEquals(GymErrorCode.NOT_FOUND.getStatus(), exception.getStatus());
//        then(gymRepository).should()
//                .findById(anyLong());
//    }
//
//    @Test
//    @DisplayName("검색어, 경도, 위도에 대해, 해당 검색어가 포함된 List<Gym>을 가까운 순으로 정렬해서 반환한다.")
//    void should_SortByDistanceAndRetrieveGymDtoList() {
//        //given
//        float currentLatitude = 10.111111f;
//        float currentLongitude = 10.111111f;
//        Gym middleGym = createGym(
//                1L,
//                "20.000001",
//                "50.543210"
//        );
//        Gym mostFarGym = createGym(
//                2L,
//                "50.123456",
//                "137.654321"
//        );
//        Gym mostNearGym = createGym(
//                3L,
//                "11.110111",
//                "12.222222"
//        );
//        List<Gym> searchedGyms = List.of(middleGym, mostFarGym, mostNearGym);
//
//        given(gymRepository.findByNameContaining(anyString()))
//                .willReturn(searchedGyms);
//
//        List<GymDto> expectedGymDtos = List.of(
//                GymDto.from(mostNearGym),
//                GymDto.from(middleGym),
//                GymDto.from(mostFarGym)
//        );
//
//        //when
//        List<GymDto> gymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc("", currentLatitude, currentLongitude);
//
//        //then
//        assertEquals(expectedGymDtos, gymDtos);
//        then(gymRepository).should()
//                .findByNameContaining(anyString());
//    }
//
//    @Test
//    @DisplayName("검색어를 포함한 List<Gym>이 비어있어도, 빈 List를 반환한다.")
//    void when_NothingSearched_Then_RetrieveEmptyList() {
//        //given
//        given(gymRepository.findByNameContaining(anyString()))
//                .willReturn(List.of());
//        List<GymDto> expectedgymDtos = List.of();
//
//        //when
//        List<GymDto> gymDtos = gymService.getGymDtosBySearchWordOrderByDistanceAsc("", 12.34f, 56.78f);
//
//        //then
//        assertEquals(expectedgymDtos, gymDtos);
//        then(gymRepository).should()
//                .findByNameContaining(anyString());
//    }
//
//    @Test
//    @DisplayName("현재 시간이 GymDto의 영업시간 사이에 있다면, true를 반환한다.")
//    void when_CurrentTimeIsBetweenBusinessHours_Then_ReturnTrue() {
//        //given
//        GymDto gymDto = createGymDto(
//                LocalTime.now()
//                        .minusMinutes(1L),
//                LocalTime.now()
//                        .plusMinutes(1L)
//        );
//
//        //when
//        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);
//
//        //then
//        assertTrue(doingBusiness);
//    }
//
//    @Test
//    @DisplayName("현재 시간이 GymDto의 영업시간 전이라면, false를 반환한다.")
//    void when_CurrentTimeIsBeforeBusinessHours_Then_ReturnFalse() {
//        //given
//        GymDto gymDto = createGymDto(
//                LocalTime.now()
//                        .plusMinutes(1L),
//                LocalTime.now()
//                        .plusMinutes(2L)
//        );
//
//        //when
//        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);
//
//        //then
//        assertFalse(doingBusiness);
//    }
//
//    @Test
//    @DisplayName("현재 시간이 GymDto의 영업시간 후이라면, false를 반환한다.")
//    void when_CurrentTimeIsAfterBusinessHours_Then_ReturnFalse() {
//        //given
//        GymDto gymDto = createGymDto(
//                LocalTime.now()
//                        .minusMinutes(2L),
//                LocalTime.now()
//                        .minusMinutes(1L)
//        );
//
//        //when
//        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);
//
//        //then
//        assertFalse(doingBusiness);
//    }
//
//    @Test
//    @DisplayName("GymDto의 현재 요일 영업시간이 비어있다면(null이라면), false를 반환한다.")
//    void when_BusinessHoursIsNull_Then_ReturnFalse() {
//        //given
//        GymDto gymDto = createGymDto(null);
//
//        //when
//        boolean doingBusiness = gymService.checkDoingBusiness(gymDto);
//
//        //then
//        assertFalse(doingBusiness);
//    }
//
//    @Test
//    @DisplayName("존재하는 gymId가 들어오면, 아무것도 하지 않는다.")
//    void when_IdOfExistingGym_Then_DoNothing() {
//        //given
//        Long gymId = 1L;
//        Gym gym = createGym(gymId);
//
//        given(gymRepository.findById(gymId))
//                .willReturn(Optional.of(gym));
//
//        //when
//        assertDoesNotThrow(
//                () -> gymService.isValidate(gymId)
//        );
//
//        //then
//        then(gymRepository).should()
//                .findById(anyLong());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 gymId가 들어오면, NOT_FOUND 예외를 반환한다.")
//    void when_IdOfNotExistingGym_Then_NotFoundException() {
//        //given
//        Long gymId = 1L;
//        Gym gym = createGym(gymId);
//
//        given(gymRepository.findById(gymId))
//                .willReturn(Optional.empty());
//
//        //when
//        BusinessException exception = assertThrows(BusinessException.class,
//                () -> gymService.isValidate(gymId)
//        );
//
//        //then
//        assertEquals(GymErrorCode.NOT_FOUND.getStatus(), exception.getStatus());
//        then(gymRepository).should()
//                .findById(anyLong());
//    }
//
//    private static Gym createGym(Long id) {
//        return Gym.of(
//                id,
//                "gymName",
//                "gymKakaoId",
//                "gymRoadAddress",
//                "gymAddress",
//                40.5f,
//                23,
//                12,
//                List.of(),
//                "123.456",
//                "123.456",
//                "gymBrand",
//                "010-1234-5678",
//                "gymInstaLink",
//                "MONDAY",
//                "11:00-23:11",
//                "12:00-23:22",
//                "13:00-23:33",
//                "14:00-23:44",
//                "15:00-23:55",
//                "16:00-23:66",
//                "17:00-23:77",
//                "gymHomepageLink",
//                "gymRemark"
//        );
//    }
//
//    private static Gym createGym(Long id, String latitude, String longitude) {
//        return Gym.of(
//                id,
//                "gymName",
//                "gymKakaoId",
//                "gymRoadAddress",
//                "gymAddress",
//                40.5f,
//                23,
//                12,
//                List.of(),
//                latitude,
//                longitude,
//                "gymBrand",
//                "010-1234-5678",
//                "gymInstaLink",
//                "MONDAY",
//                "11:00-23:11",
//                "12:00-23:22",
//                "13:00-23:33",
//                "14:00-23:44",
//                "15:00-23:55",
//                "16:00-23:66",
//                "17:00-23:77",
//                "gymHomepageLink",
//                "gymRemark"
//        );
//    }
//
//    private static GymDto createGymDto(LocalTime openTime, LocalTime closeTime) {
//        return GymDto.of(
//                1L,
//                "더클라임 봉은사점",
//                "kakaoid",
//                "서울시 도로명주소",
//                "서울시 지번주소",
//                40.5f,
//                23,
//                12,
//                List.of(),
//                "37.513709",
//                "127.062144",
//                "더클라임",
//                "01012345678",
//                "gymInstagramLink.com",
//                "MONDAY",
//                LocalDateTime.of(1999, 3, 1, 7, 30),
//                LocalDateTime.of(2024, 1, 23, 18, 32),
//                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
//                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
//                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
//                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
//                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
//                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
//                openTime.getHour() + ":" + openTime.getMinute() + "-" + closeTime.getHour() + ":" + closeTime.getMinute(),
//                "gymHomepageLink",
//                "gymRemark"
//        );
//    }
//
//    private static GymDto createGymDto(String businessHour) {
//        return GymDto.of(
//                1L,
//                "더클라임 봉은사점",
//                "kakaoid",
//                "서울시 도로명주소",
//                "서울시 지번주소",
//                40.5f,
//                23,
//                12,
//                List.of(),
//                "37.513709",
//                "127.062144",
//                "더클라임",
//                "01012345678",
//                "gymInstagramLink.com",
//                "MONDAY",
//                LocalDateTime.of(1999, 3, 1, 7, 30),
//                LocalDateTime.of(2024, 1, 23, 18, 32),
//                businessHour,
//                businessHour,
//                businessHour,
//                businessHour,
//                businessHour,
//                businessHour,
//                businessHour,
//                "gymHomepageLink",
//                "gymRemark"
//        );
//    }
//}
