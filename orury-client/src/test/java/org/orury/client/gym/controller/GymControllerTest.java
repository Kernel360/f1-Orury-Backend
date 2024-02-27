//package org.orury.client.gym.controller;
//
//import org.orury.client.config.ControllerTest;
//import org.orury.client.config.WithUserPrincipal;
//import org.orury.client.gym.converter.message.GymMessage;
//import org.orury.common.error.code.GymErrorCode;
//import org.orury.common.error.exception.BusinessException;
//import org.orury.domain.global.constants.NumberConstants;
//import org.orury.domain.gym.dto.GymDto;
//import org.orury.domain.review.dto.ReviewDto;
//import org.orury.domain.user.entity.model.User;
//import org.orury.domain.user.dto.UserDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@DisplayName("[Controller] 암장 관련 테스트")
//@WithUserPrincipal
//class GymControllerTest extends ControllerTest {
//
//    @DisplayName("[GET] 암장 id로 암장 상세 조회 - 성공 (암장에 달린 리뷰 있는 경우)")
//    @Test
//    void when_GymId_Then_GymDetailSuccessfully() throws Exception {
//        //given
//        Long userId = NumberConstants.USER_ID;
//        Long gymId = 1L;
//        GymDto gymDto = createGymDto(gymId);
//
//        ReviewDto review1 = createReviewDto(1L, gymDto, 11L);
//        ReviewDto review2 = createReviewDto(1L, gymDto, 22L);
//        ReviewDto review3 = createReviewDto(1L, gymDto, 33L);
//        List<ReviewDto> reviewDtos = List.of(review1, review2, review3);
//
//        GymMessage message = GymMessage.GYM_READ;
//
//        given(gymService.getGymDtoById(anyLong()))
//                .willReturn(gymDto);
//        given(gymService.checkDoingBusiness(any()))
//                .willReturn(true);
//        given(gymLikeService.isLiked(anyLong(), anyLong()))
//                .willReturn(false);
//        given(reviewService.getAllReviewDtosByGymId(anyLong()))
//                .willReturn(reviewDtos);
//
//        //when & then
//        mvc.perform(get("/api/v1/gyms/" + gymId).accept("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(message.getMessage()))
//                .andExpect(jsonPath("$.data.id").value(gymId))
//                .andExpect(jsonPath("$.data.name").value(gymDto.name()))
//                .andExpect(jsonPath("$.data.doing_business").value(true))
//                .andExpect(jsonPath("$.data.is_like").value(false))
//                .andExpect(jsonPath("$.data.bar_chart_data").isNotEmpty())
//                .andExpect(jsonPath("$.data.line_chart_data").isNotEmpty())
//        ;
//
//        then(gymService).should()
//                .getGymDtoById(gymId);
//        then(gymService).should()
//                .checkDoingBusiness(gymDto);
//        then(gymLikeService).should()
//                .isLiked(userId, gymId);
//        then(reviewService).should()
//                .getAllReviewDtosByGymId(gymId);
//    }
//
//    @DisplayName("[GET] 암장 id로 암장 상세 조회 - 성공 (암장에 달린 리뷰 없는 경우)")
//    @Test
//    void when_GymIdAndNoReviews_Then_GymDetailSuccessfully() throws Exception {
//        //given
//        Long userId = NumberConstants.USER_ID;
//        Long gymId = 1L;
//        GymDto gymDto = createGymDto(gymId);
//
//        List<ReviewDto> reviewDtos = List.of();
//
//        GymMessage message = GymMessage.GYM_READ;
//
//        given(gymService.getGymDtoById(anyLong()))
//                .willReturn(gymDto);
//        given(gymService.checkDoingBusiness(any()))
//                .willReturn(false);
//        given(gymLikeService.isLiked(anyLong(), anyLong()))
//                .willReturn(true);
//        given(reviewService.getAllReviewDtosByGymId(anyLong()))
//                .willReturn(reviewDtos);
//
//
//        //when & then
//        mvc.perform(get("/api/v1/gyms/" + gymId).accept("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(message.getMessage()))
//                .andExpect(jsonPath("$.data.id").value(gymId))
//                .andExpect(jsonPath("$.data.name").value(gymDto.name()))
//                .andExpect(jsonPath("$.data.doing_business").value(false))
//                .andExpect(jsonPath("$.data.is_like").value(true))
//                .andExpect(jsonPath("$.data.bar_chart_data").isEmpty())
//                .andExpect(jsonPath("$.data.line_chart_data").isEmpty())
//        ;
//
//        then(gymService).should()
//                .getGymDtoById(gymId);
//        then(gymService).should()
//                .checkDoingBusiness(gymDto);
//        then(gymLikeService).should()
//                .isLiked(userId, gymId);
//        then(reviewService).should()
//                .getAllReviewDtosByGymId(gymId);
//    }
//
//    @DisplayName("[GET] 암장 id로 암장 상세 조회 - 실패 (존재하지 않는 암장)")
//    @Test
//    void when_NotExistingGymId_Then_NotFoundException() throws Exception {
//        //given
//        Long userId = NumberConstants.USER_ID;
//        Long gymId = 1L;
//
//        GymErrorCode code = GymErrorCode.NOT_FOUND;
//
//        given(gymService.getGymDtoById(anyLong()))
//                .willThrow(new BusinessException(code));
//
//        //when & then
//        mvc.perform(get("/api/v1/gyms/" + gymId).accept("application/json"))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(gymService).should()
//                .getGymDtoById(gymId);
//        then(gymService).should(never())
//                .checkDoingBusiness(any());
//        then(gymLikeService).should(never())
//                .isLiked(userId, gymId);
//        then(reviewService).should(never())
//                .getAllReviewDtosByGymId(gymId);
//    }
//
//    @DisplayName("[GET] 검색어와 위치 좌표로 암장 목록 조회 - 성공 (검색어 포함하는 암장 있는 경우)")
//    @Test
//    void when_ExistingSearchResult_Then_GymDtosSuccessFully() throws Exception {
//        //given
//        Long userId = NumberConstants.USER_ID;
//        String searchWord = "SearchWord for TestCode";
//        float latitude = 123.456f;
//        float longitude = 123.456f;
//
//        GymDto gymDto1 = createGymDto(1L);
//        GymDto gymDto2 = createGymDto(2L);
//        GymDto gymDto3 = createGymDto(3L);
//        List<GymDto> gymDtos = List.of(gymDto1, gymDto2, gymDto3);
//
//        GymMessage message = GymMessage.GYM_READ;
//
//        when(gymService.getGymDtosBySearchWordOrderByDistanceAsc(searchWord, latitude, longitude))
//                .thenReturn(gymDtos);
//        given(gymLikeService.isLiked(userId, 1L))
//                .willReturn(true);
//        given(gymLikeService.isLiked(userId, 2L))
//                .willReturn(false);
//        given(gymLikeService.isLiked(userId, 3L))
//                .willReturn(true);
//        given(gymService.checkDoingBusiness(gymDto1))
//                .willReturn(false);
//        given(gymService.checkDoingBusiness(gymDto2))
//                .willReturn(true);
//        given(gymService.checkDoingBusiness(gymDto3))
//                .willReturn(false);
//
//        //when & then
//        mvc.perform(get("/api/v1/gyms/search")
//                        .param("search_word", searchWord)
//                        .param("latitude", String.valueOf(latitude))
//                        .param("longitude", String.valueOf(longitude))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(message.getMessage()))
//                .andExpect(jsonPath("$.data[0].is_like").value(true))
//                .andExpect(jsonPath("$.data[1].is_like").value(false))
//                .andExpect(jsonPath("$.data[2].is_like").value(true))
//                .andExpect(jsonPath("$.data[0].doing_business").value(false))
//                .andExpect(jsonPath("$.data[1].doing_business").value(true))
//                .andExpect(jsonPath("$.data[2].doing_business").value(false))
//                .andExpect(jsonPath("$.message").value(message.getMessage()))
//        ;
//
//        then(gymService).should(times(1))
//                .getGymDtosBySearchWordOrderByDistanceAsc(anyString(), anyFloat(), anyFloat());
//        then(gymLikeService).should(times(3))
//                .isLiked(anyLong(), anyLong());
//        then(gymService).should(times(3))
//                .checkDoingBusiness(any());
//    }
//
//    @DisplayName("[GET] 검색어와 위치 좌표로 암장 목록 조회 - 성공 (검색어 포함하는 암장 없는 경우)")
//    @Test
//    void when_NotExistingSearchResult_Then_NothingInData() throws Exception {
//        //given
//        String searchWord = "SearchWord for TestCode";
//        float latitude = 123.456f;
//        float longitude = 123.456f;
//        List<GymDto> gymDtos = List.of();
//
//        GymMessage message = GymMessage.GYM_READ;
//
//        given(gymService.getGymDtosBySearchWordOrderByDistanceAsc(anyString(), anyFloat(), anyFloat()))
//                .willReturn(gymDtos);
//
//        //when & then
//        mvc.perform(get("/api/v1/gyms/search")
//                        .param("search_word", searchWord)
//                        .param("latitude", String.valueOf(latitude))
//                        .param("longitude", String.valueOf(longitude))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(message.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//                .andExpect(jsonPath("$.message").value(message.getMessage()))
//        ;
//
//        then(gymService).should()
//                .getGymDtosBySearchWordOrderByDistanceAsc(anyString(), anyFloat(), anyFloat());
//        then(gymLikeService).should(never())
//                .isLiked(anyLong(), anyLong());
//        then(gymService).should(never())
//                .checkDoingBusiness(any());
//    }
//
//
//    private static GymDto createGymDto(Long gymId) {
//        return GymDto.of(
//                gymId,
//                "더클라임 봉은사점",
//                "kakaoid",
//                "서울시 도로명주소",
//                "서울시 지번주소",
//                4.5f,
//                12,
//                11,
//                List.of(),
//                "37.513709",
//                "127.062144",
//                "더클라임",
//                "01012345678",
//                "gymInstagramLink.com",
//                "MONDAY",
//                LocalDateTime.of(1999, 3, 1, 7, 30),
//                LocalDateTime.of(2024, 1, 23, 18, 32),
//                "11:11-23:11",
//                "11:22-23:22",
//                "11:33-23:33",
//                "11:44-23:44",
//                "11:55-23:55",
//                "11:66-23:66",
//                "11:77-23:77",
//                "gymHomepageLink",
//                "gymRemark"
//        );
//    }
//
//    private static User createUser(Long id) {
//        return User.of(
//                id,
//                "userEmail",
//                "userNickname",
//                "userPassword",
//                1,
//                1,
//                null,
//                "userProfileImage",
//                null,
//                null,
//                NumberConstants.IS_NOT_DELETED
//        );
//    }
//
//    private static ReviewDto createReviewDto(Long id, GymDto gymDto, Long userId) {
//        return ReviewDto.of(
//                id,
//                "reviewContent",
//                List.of(),
//                4.5f,
//                0,
//                1,
//                2,
//                3,
//                4,
//                UserDto.from(createUser(userId)),
//                gymDto,
//                LocalDateTime.of(2024, 1, 1, 14, 23),
//                LocalDateTime.of(2024, 1, 25, 4, 56)
//        );
//    }
//}
