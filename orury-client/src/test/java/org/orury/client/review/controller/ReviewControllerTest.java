//package org.orury.client.review.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.orury.client.config.ControllerTest;
//import org.orury.client.config.WithUserPrincipal;
//import org.orury.client.review.converter.message.ReviewMessage;
//import org.orury.client.review.converter.request.ReviewCreateRequest;
//import org.orury.client.review.converter.request.ReviewUpdateRequest;
//import org.orury.client.review.converter.response.ReviewsResponse;
//import org.orury.client.review.converter.response.ReviewsWithCursorResponse;
//import org.orury.common.error.code.GymErrorCode;
//import org.orury.common.error.code.ReviewErrorCode;
//import org.orury.common.error.code.UserErrorCode;
//import org.orury.common.error.exception.BusinessException;
//import org.orury.domain.global.constants.NumberConstants;
//import org.orury.domain.gym.domain.dto.GymDto;
//import org.orury.domain.review.dto.ReviewDto;
//import org.orury.domain.user.dto.UserDto;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.*;
//import static org.springframework.http.HttpMethod.PATCH;
//import static org.springframework.http.HttpMethod.POST;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
////@Disabled
//@DisplayName("[Controller] 리뷰 관련 테스트")
//@WithUserPrincipal
//class ReviewControllerTest extends ControllerTest {
//
//    @DisplayName("[POST] 유효한 요청이고, 이미지가 있을 때, 리뷰를 생성한다. (이미지 있는 경우)- 성공")
//    @Test
//    void given_ValidateIdAndRequestReviewAndExistImage_When_CreateReview_Then_Successfully() throws Exception {
//        //given
//        UserDto userDto = createUserDto();
//        GymDto gymDto = createGymDto();
//
//        ReviewCreateRequest reviewCreateRequest = createReviewCreateRequest();
//        MockMultipartFile request = createRequestPart(reviewCreateRequest);
//        MockMultipartFile image = createImagePart();
//
//        ReviewMessage code = ReviewMessage.REVIEW_CREATED;
//
//        given(userService.getUserDtoById(anyLong())).willReturn(userDto);
//        given(gymService.getGymDtoById(anyLong())).willReturn(gymDto);
//
//        //when & then
//        mvc.perform(multipart(POST, "/api/v1/reviews")
//                        .file(request)
//                        .file(image)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//        ;
//
//        then(userService).should(times(1)).getUserDtoById(anyLong());
//        then(gymService).should(times(1)).getGymDtoById(anyLong());
//        then(reviewService).should(times(1)).isExist(any(), any());
//        then(reviewService).should(times(1)).createReview(any(), any());
//    }
//
//    @DisplayName("[POST] 유효한 요청이고, 이미지가 없을 때, 리뷰를 생성한다. (이미지 없는 경우)- 성공")
//    @Test
//    void given_ValidateIdAndRequestReviewAndNotExistImage_When_CreateReview_Then_Successfully() throws Exception {
//        //given
//        UserDto userDto = createUserDto();
//        GymDto gymDto = createGymDto();
//
//        ReviewCreateRequest reviewCreateRequest = createReviewCreateRequest();
//        MockMultipartFile request = createRequestPart(reviewCreateRequest);
//
//        ReviewMessage code = ReviewMessage.REVIEW_CREATED;
//
//        given(userService.getUserDtoById(anyLong())).willReturn(userDto);
//        given(gymService.getGymDtoById(anyLong())).willReturn(gymDto);
//
//        //when & then
//        mvc.perform(multipart(POST, "/api/v1/reviews")
//                        .file(request)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//        ;
//
//        then(userService).should(times(1)).getUserDtoById(anyLong());
//        then(gymService).should(times(1)).getGymDtoById(anyLong());
//        then(reviewService).should(times(1)).isExist(any(), any());
//        then(reviewService).should(times(1)).createReview(any(), any());
//    }
//
//    @DisplayName("[POST] 인증된 UserId로 UserDto를 가져오는 것을 실패했을 경우, 리뷰를 생성 시 예외 발생 - 실패")
//    @Test
//    void given_NotValidUserId_When_CreateReview_Then_NotFoundException() throws Exception {
//        //given
//        ReviewCreateRequest reviewCreateRequest = createReviewCreateRequest();
//        MockMultipartFile request = createRequestPart(reviewCreateRequest);
//        ReviewDto reviewDto = createReviewDto(1L);
//        UserErrorCode code = UserErrorCode.NOT_FOUND;
//
//        given(userService.getUserDtoById(anyLong())).willThrow(new BusinessException(code));
//
//        //when & then
//        mvc.perform(multipart(POST, "/api/v1/reviews")
//                        .file(request)
//                        .with(csrf()))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(userService).should(times(1))
//                .getUserDtoById(anyLong());
//        then(gymService).should(times(0))
//                .getGymDtoById(anyLong());
//        then(reviewService).should(times(0))
//                .isExist(any(), any());
//        then(reviewService).should(times(0))
//                .createReview(reviewDto, List.of());
//    }
//
//    @DisplayName("[POST] 유효하지 않은 gymId를 포함한 리뷰 정보를 받아, 리뷰를 생성 시 예외 발생 - 실패")
//    @Test
//    void given_UserIdAndRequestReview_When_CreateReview_Then_NotFoundException() throws Exception {
//        //given
//        UserDto userDto = createUserDto();
//        ReviewCreateRequest reviewCreateRequest = createReviewCreateRequest();
//        MockMultipartFile request = createRequestPart(reviewCreateRequest);
//        GymErrorCode code = GymErrorCode.NOT_FOUND;
//
//        given(userService.getUserDtoById(anyLong())).willReturn(userDto);
//        given(gymService.getGymDtoById(anyLong())).willThrow(new BusinessException(code));
//
//        //when & then
//        mvc.perform(multipart(POST, "/api/v1/reviews")
//                        .file(request)
//                        .with(csrf()))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(userService).should(times(1))
//                .getUserDtoById(anyLong());
//        then(gymService).should(times(1))
//                .getGymDtoById(anyLong());
//        then(reviewService).should(times(0))
//                .isExist(any(), any());
//        then(reviewService).should(times(0))
//                .createReview(any(), anyList());
//    }
//
//    @DisplayName("[POST] 이미 작성된 리뷰가 있을 경우, 리뷰를 생성 시 예외 발생 - 실패")
//    @Test
//    void given_ExistReview_When_CreateReview_Then_BadRequestException() throws Exception {
//        //given
//        UserDto userDto = createUserDto();
//        GymDto gymDto = createGymDto();
//        ReviewCreateRequest reviewCreateRequest = createReviewCreateRequest();
//        MockMultipartFile request = createRequestPart(reviewCreateRequest);
//
//        ReviewErrorCode code = ReviewErrorCode.BAD_REQUEST;
//
//        given(userService.getUserDtoById(anyLong())).willReturn(userDto);
//        given(gymService.getGymDtoById(anyLong())).willReturn(gymDto);
//        willThrow(new BusinessException(code)).given(reviewService).isExist(any(), any());
//
//        //when & then
//        mvc.perform(multipart(POST, "/api/v1/reviews")
//                        .file(request)
//                        .with(csrf()))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(userService).should(times(1)).getUserDtoById(anyLong());
//        then(gymService).should(times(1)).getGymDtoById(anyLong());
//        then(reviewService).should(times(1)).isExist(any(), any());
//        then(reviewService).should(times(0)).createReview(any(), anyList());
//    }
//
//    @DisplayName("[PATCH] 기존 리뷰를 불러온 후, 수정할 리뷰 정보를 받아, 리뷰를 수정한다.(이미지 없는 경우) - 성공")
//    @Test
//    void given_RequestUpdateReview_When_UpdateRequest_Then_Successfully() throws Exception {
//        // given
//        ReviewDto beforeReviewDto = createReviewDto(1L);
//        ReviewUpdateRequest reviewUpdateRequest = createReviewUpdateRequest();
//        MockMultipartFile request = createRequestPart(reviewUpdateRequest);
//
//        ReviewMessage code = ReviewMessage.REVIEW_UPDATED;
//
//        given(reviewService.getReviewDtoById(anyLong())).willReturn(beforeReviewDto);
//
//        //when & then
//        mvc.perform(multipart(PATCH, "/api/v1/reviews/" + 1L)
//                        .file(request)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//        ;
//
//        then(reviewService).should(times(1)).getReviewDtoById(any());
//        then(reviewService).should(times(1)).isValidate(anyLong(), anyLong());
//        then(reviewService).should(times(1)).updateReview(any(), any(), any());
//    }
//
//    @DisplayName("[PATCH] 기존 리뷰를 불러온 후, 수정할 리뷰 정보를 받아, 리뷰를 수정한다. (이미지 있는 경우) - 성공")
//    @Test
//    void given_RequestUpdateReviewAndExistImage_When_UpdateRequest_Then_Successfully() throws Exception {
//        // given
//        ReviewDto beforeReviewDto = createReviewDto(1L);
//        ReviewUpdateRequest reviewUpdateRequest = createReviewUpdateRequest();
//        MockMultipartFile request = createRequestPart(reviewUpdateRequest);
//        MockMultipartFile image = createImagePart();
//
//        ReviewMessage code = ReviewMessage.REVIEW_UPDATED;
//
//        given(reviewService.getReviewDtoById(anyLong())).willReturn(beforeReviewDto);
//
//        //when & then
//        mvc.perform(multipart(PATCH, "/api/v1/reviews/" + 1L)
//                        .file(request)
//                        .file(image)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//        ;
//
//        then(reviewService).should(times(1)).getReviewDtoById(any());
//        then(reviewService).should(times(1)).isValidate(anyLong(), anyLong());
//        then(reviewService).should(times(1)).updateReview(any(), any(), any());
//    }
//
//    @DisplayName("[PATCH] 기존에 작성한 리뷰가 없을 경우, 리뷰 수정 실패로 예외 처리 발생 - 실패")
//    @Test
//    void given_NotExistReview_When_UpdateRequest_Then_NotFoundException() throws Exception {
//        // given
//        ReviewUpdateRequest reviewUpdateRequest = createReviewUpdateRequest();
//        MockMultipartFile request = createRequestPart(reviewUpdateRequest);
//        ReviewErrorCode code = ReviewErrorCode.NOT_FOUND;
//
//        given(reviewService.getReviewDtoById(anyLong())).willThrow(new BusinessException(code));
//
//        //when & then
//        mvc.perform(multipart(PATCH, "/api/v1/reviews/" + 1L)
//                        .file(request)
//                        .with(csrf()))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(reviewService).should(times(1)).getReviewDtoById(any());
//        then(reviewService).should(times(0)).isValidate(anyLong(), anyLong());
//        then(reviewService).should(times(0)).updateReview(any(), any(), any());
//    }
//
//    @DisplayName("[PATCH] 리뷰 작성자와 수정 요청자의 id가 일치하지 않는 경우, 리뷰 수정 실패로 예외 처리 발생 - 실패")
//    @Test
//    void given_NotMatchUserId_When_UpdateRequest_Then_ForbiddenException() throws Exception {
//        //given
//        ReviewDto beforeReviewDto = createReviewDto(1L);
//        ReviewUpdateRequest reviewUpdateRequest = createReviewUpdateRequest();
//        MockMultipartFile request = createRequestPart(reviewUpdateRequest);
//        ReviewErrorCode code = ReviewErrorCode.FORBIDDEN;
//
//        given(reviewService.getReviewDtoById(anyLong())).willReturn(beforeReviewDto);
//        willThrow(new BusinessException(code)).given(reviewService).isValidate(anyLong(), anyLong());
//
//        //when & then
//        mvc.perform(multipart(PATCH, "/api/v1/reviews/" + 1L)
//                        .file(request)
//                        .with(csrf()))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(reviewService).should(times(1)).getReviewDtoById(any());
//        then(reviewService).should(times(1)).isValidate(anyLong(), anyLong());
//        then(reviewService).should(times(0)).updateReview(any(), any(), any());
//    }
//
//    @DisplayName("[Delete] 리뷰 id를 받아, 리뷰를 삭제한다. - 성공")
//    @Test
//    void given_RequestDeleteReview_When_DeleteReview_Then_Successfully() throws Exception {
//        //given
//        ReviewDto deleteReviewDto = createReviewDto(1L);
//        ReviewMessage code = ReviewMessage.REVIEW_DELETED;
//
//        given(reviewService.getReviewDtoById(anyLong())).willReturn(deleteReviewDto);
//
//        //when & then
//        mvc.perform(delete("/api/v1/reviews/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data").isEmpty())
//        ;
//
//        then(reviewService).should(times(1)).getReviewDtoById(any());
//        then(reviewService).should(times(1)).isValidate(anyLong(), anyLong());
//        then(reviewService).should(times(1)).deleteReview(any());
//    }
//
//    @DisplayName("[Delete] 삭제할 리뷰가 존재하지 않는 경우, 리뷰 삭제 예외 발생. - 실패")
//    @Test
//    void given_NotExistDeleteReview_When_DeleteReview_Then_NotFoundException() throws Exception {
//        //given
//        ReviewErrorCode code = ReviewErrorCode.NOT_FOUND;
//
//        given(reviewService.getReviewDtoById(anyLong())).willThrow(new BusinessException(code));
//
//        //when & then
//        mvc.perform(delete("/api/v1/reviews/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(reviewService).should(times(1)).getReviewDtoById(any());
//        then(reviewService).should(times(0)).isValidate(anyLong(), anyLong());
//        then(reviewService).should(times(0)).deleteReview(any());
//    }
//
//    @DisplayName("[Delete] 리뷰 작성자와 삭제 요청자의 id가 일치하지 않는 경우, 리뷰 삭제 예외 발생. - 실패")
//    @Test
//    void given_NotMatchUserId_When_DeleteReview_Then_ForbiddenException() throws Exception {
//        //given
//        ReviewDto deleteReviewDto = createReviewDto(1L);
//        ReviewErrorCode code = ReviewErrorCode.FORBIDDEN;
//
//        given(reviewService.getReviewDtoById(anyLong())).willReturn(deleteReviewDto);
//        willThrow(new BusinessException(code)).given(reviewService).isValidate(anyLong(), anyLong());
//
//        //when & then
//        mvc.perform(delete("/api/v1/reviews/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(reviewService).should(times(1)).getReviewDtoById(any());
//        then(reviewService).should(times(1)).isValidate(anyLong(), anyLong());
//        then(reviewService).should(times(0)).deleteReview(any());
//    }
//
//    @DisplayName("[GET] 암장 id를 받아 해당 암장의 리뷰를 반환한다. - 성공")
//    @Test
//    void given_GymIdAndCursor_When_GetReviewsByGym_Then_Successfully() throws Exception {
//        //given
//        Long cursor = 1L;
//        GymDto gymDto = createGymDto();
//        //Long userPrincipalId = NumberConstants.USER_ID;
//        UserDto userDto = createUserDto();
//        ReviewMessage code = ReviewMessage.REVIEWS_READ;
//
//        List<ReviewDto> reviewDtos = new ArrayList<>();
//        for (int i = 1; i <= NumberConstants.POST_PAGINATION_SIZE; i++) {
//            reviewDtos.add(createReviewDto((long) i));
//        }
//
//        List<ReviewsResponse> reviewsResponses = reviewDtos.stream()
//                .map(reviewDto -> {
//                    int myReaction = NumberConstants.INTERREST_REACTION;
//                    given(reviewReactionService.getReactionType(userDto.id(), reviewDto.id())).willReturn(myReaction);
//                    return ReviewsResponse.of(reviewDto, userDto, myReaction);
//                })
//                .toList();
//
//        ReviewsWithCursorResponse response = ReviewsWithCursorResponse.of(reviewsResponses, gymDto.name());
//
//        given(gymService.getGymDtoById(anyLong())).willReturn(gymDto);
//        given(userService.getUserDtoById(anyLong())).willReturn(userDto);
//        given(reviewService.getReviewDtosByGymId(anyLong(), anyLong(), any())).willReturn(reviewDtos);
//
//        //when & then
//        mvc.perform(get("/api/v1/reviews/gym/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                        .param("cursor", "" + cursor))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//                .andExpect(jsonPath("$.data.reviews[0].id").value(response.reviews().get(0).id()))
//                .andExpect(jsonPath("$.data.reviews[0].content").value(response.reviews().get(0).content()))
//                .andExpect(jsonPath("$.data.cursor").value(response.cursor()))
//                .andExpect(jsonPath("$.data.gym_name").value(response.gymName()))
//        ;
//
//        then(gymService).should(times(1)).getGymDtoById(any());
//        then(reviewService).should(times(1)).getReviewDtosByGymId(anyLong(), anyLong(), any());
//        then(reviewReactionService).should(times(reviewDtos.size())).getReactionType(anyLong(), anyLong());
//    }
//
//    @DisplayName("[GET] 암장 id가 유효하지 않은 경우, 리뷰 조회 실패 예외처리 발생 - 실패")
//    @Test
//    void given_NotExistGym_When_GetReviewsByGym_Then_NotFoundException() throws Exception {
//        //given
//        Long gymId = 1L;
//        ReviewErrorCode code = ReviewErrorCode.NOT_FOUND;
//
//        given(gymService.getGymDtoById(anyLong())).willThrow(new BusinessException(code));
//
//        //when & then
//        mvc.perform(get("/api/v1/reviews/gym/" + 1L)
//                        .with(csrf())
//                        .contentType(APPLICATION_JSON)
//                        .param("cursor", "" + 1L))
//                .andExpect(status().is(code.getStatus()))
//                .andExpect(jsonPath("$.message").value(code.getMessage()))
//        ;
//
//        then(reviewService).should(times(0)).getReviewDtosByGymId(anyLong(), anyLong(), any());
//        then(reviewReactionService).should(times(0)).getReactionType(anyLong(), anyLong());
//        then(gymService).should(times(1)).getGymDtoById(any());
//    }
//
//
//    private UserDto createUserDto() {
//        return UserDto.of(
//                1L,
//                "mail@mail",
//                "name",
//                "pw",
//                1,
//                1,
//                LocalDate.now(),
//                null,
//                null,
//                null,
//                NumberConstants.IS_NOT_DELETED
//        );
//    }
//
//    private GymDto createGymDto() {
//        return GymDto.of(
//                1L,
//                "더클라임 봉은사점",
//                "kakaoid",
//                "서울시 도로명주소",
//                "서울시 지번주소",
//                25.3f,
//                23,
//                12,
//                List.of(),
//                "37.513709",
//                "127.062144",
//                "더클라임",
//                "01012345678",
//                "instalink.com",
//                "MONDAY",
//                LocalDateTime.now(),
//                LocalDateTime.now(),
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
//    private ReviewDto createReviewDto(Long id) {
//        return ReviewDto.of(
//                id,
//                "review 내용",
//                List.of(),
//                4.5f,
//                1,
//                2,
//                3,
//                4,
//                5,
//                createUserDto(),
//                createGymDto(),
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//    }
//
//    private ReviewCreateRequest createReviewCreateRequest() {
//        return ReviewCreateRequest.of(
//                "여기 암장 좀 괜찮네요",
//                5.0f,
//                1L
//        );
//    }
//
//    private ReviewUpdateRequest createReviewUpdateRequest() {
//        return ReviewUpdateRequest.of(
//                "update review content",
//                4.0f
//        );
//    }
//
//    private MockMultipartFile createImagePart() {
//        return new MockMultipartFile(
//                "TestImageName",
//                "testImageFileName",
//                MediaType.TEXT_PLAIN_VALUE,
//                "testImageFileDate".getBytes()
//        );
//    }
//
//    private MockMultipartFile createRequestPart(Object request) throws JsonProcessingException {
//        return new MockMultipartFile(
//                "request",
//                "testRequest",
//                MediaType.APPLICATION_JSON_VALUE,
//                mapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
//        );
//    }
//
//
//}