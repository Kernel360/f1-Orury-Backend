package org.orury.client.review.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.gym.application.GymService;
import org.orury.client.review.interfaces.request.ReviewCreateRequest;
import org.orury.client.review.interfaces.request.ReviewReactionRequest;
import org.orury.client.review.interfaces.request.ReviewUpdateRequest;
import org.orury.client.review.interfaces.response.ReviewsResponse;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Facade] 리뷰 Facade 테스트")
@ActiveProfiles("test")
class ReviewFacadeTest {
    private ReviewService reviewService;
    private UserService userService;
    private GymService gymService;
    private ReviewFacade reviewFacade;

    @BeforeEach
    void setUp() {
        reviewService = mock(ReviewService.class);
        userService = mock(UserService.class);
        gymService = mock(GymService.class);
        reviewFacade = new ReviewFacade(reviewService, userService, gymService);
    }

    @DisplayName("리뷰 생성 요청이 들어왔을 때, 리뷰를 성공적으로 저장한다.")
    @Test
    void should_SaveReviewSuccessfully() {
        // given
        UserDto userDto = createUserDto();
        GymDto gymDto = createGymDto();
        ReviewCreateRequest request = createReviewCreateRequest();
        List<MultipartFile> images = createMultiFiles();

        given(userService.getUserDtoById(1L)).willReturn(userDto);
        given(gymService.getGymDtoById(1L)).willReturn(gymDto);

        // when
        reviewFacade.createReview(1L, request, images);

        // then
        then(reviewService).should(times(1)).createReview(any(), any());
    }

    @DisplayName("리뷰 수정 요청이 들어왔을 때, 리뷰를 성공적으로 수정한다.")
    @Test
    void should_UpdateReviewSuccessfully() {
        // given
        ReviewUpdateRequest request = createReviewUpdateRequest();
        Long reviewId = 1L;
        Long userId = 1L;
        List<MultipartFile> images = createMultiFiles();
        ReviewDto beforeReviewDto = createReviewDto(reviewId);
        given(reviewService.getReviewDtoById(reviewId, userId)).willReturn(beforeReviewDto);
        ReviewDto updatedReviewDto = request.toDto(beforeReviewDto);

        // when
        reviewFacade.updateReview(reviewId, userId, request, images);

        // then
        then(reviewService).should(times(1))
                .updateReview(beforeReviewDto, updatedReviewDto, images);
    }

    @DisplayName("리뷰ID와 유저ID를 받아 리뷰를 삭제시킨다.")
    @Test
    void should_DeleteReviewSuccessfully() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        ReviewDto reviewDto = createReviewDto(reviewId);
        given(reviewService.getReviewDtoById(reviewId, userId)).willReturn(reviewDto);

        // when
        reviewFacade.deleteReview(reviewId, userId);

        // then
        then(reviewService).should(times(1))
                .deleteReview(reviewDto);
    }

    @DisplayName("암장ID, 유저ID, 커서를 받아 암장의 리뷰를 조회한다.")
    @Test
    void when_GetGymIdAndUserIdAndCursor_Then_RetrieveReviews() {
        // given
        String gymName = "더클라임 봉은사점";
        Long gymId = 1L;
        Long userId = 1L;
        Long cursor = 1L;
        GymDto gymDto = createGymDto(gymName);
        List<ReviewDto> reviewDtos = List.of(
                createReviewDto(1L),
                createReviewDto(2L),
                createReviewDto(3L)
        );
        List<ReviewsResponse> reviewsResponses = reviewDtos.stream()
                .map(reviewDto -> {
                    int myReaction = 1;
                    return ReviewsResponse.of(reviewDto, userId, myReaction);
                })
                .toList();

        given(reviewService.getReactionType(anyLong(), anyLong())).willReturn(1); // 또는 원하는 int 값
        given(gymService.getGymDtoById(gymId)).willReturn(gymDto);
        given(reviewService.getReviewDtosByGymId(gymId, cursor, PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE)))
                .willReturn(reviewDtos);

        // when
        reviewFacade.getGymReviews(gymId, userId, cursor);

        // then
        then(gymService).should(times(1))
                .getGymDtoById(gymId);
        then(reviewService).should(times(1))
                .getReviewDtosByGymId(anyLong(), anyLong(), any());
        then(reviewService).should(times(reviewsResponses.size()))
                .getReactionType(anyLong(), anyLong());
    }

    @DisplayName("리뷰 반응 업데이트 요청이 들어오면, 이를 생성/수정/삭제한다.")
    @Test
    void should_ProcessRevicewReaction_Successfully() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        ReviewReactionRequest request = createReviewReactionRequest();
        ReviewReactionPK reactionPK = createReviewReactionPK(userId, reviewId);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.of(reactionPK, request.reactionType());

        // when
        reviewFacade.processReviewReaction(reviewId, request, userId);

        // then
        then(reviewService).should(times(1))
                .processReviewReaction(reviewReactionDto);
    }

    private List<ReviewDto> createReviewDtos() {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (int i = 1; i <= NumberConstants.POST_PAGINATION_SIZE; i++) {
            reviewDtos.add(createReviewDto((long) i));
        }
        return reviewDtos;
    }

    public List<MultipartFile> createMultiFiles() {
        try {
            // 여러 개의 MultipartFile을 생성하여 배열에 담아 반환
            return List.of(
                    createMockMultipartFile("key1", "image.png"),
                    createMockMultipartFile("key2", "image2.png")
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to create MultipartFile.", e);
        }
    }

    private UserDto createUserDto() {
        return UserDto.of(
                1L,
                "mail@mail",
                "name",
                "pw",
                1,
                1,
                LocalDate.now(),
                null,
                null,
                null,
                NumberConstants.IS_NOT_DELETED
        );
    }

    private GymDto createGymDto(String gymName) {
        return GymDto.of(
                1L,
                gymName,
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                25.3f,
                23,
                12,
                List.of(),
                "37.513709",
                "127.062144",
                "더클라임",
                "01012345678",
                "instalink.com",
                "MONDAY",
                LocalDateTime.now(),
                LocalDateTime.now(),
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

    private ReviewDto createReviewDto(Long id) {
        return ReviewDto.of(
                id,
                "review 내용",
                List.of(),
                4.5f,
                1,
                2,
                3,
                4,
                5,
                createUserDto(),
                createGymDto(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private GymDto createGymDto() {
        return GymDto.of(
                1L,
                "더클라임 봉은사점",
                "kakaoid",
                "서울시 도로명주소",
                "서울시 지번주소",
                25.3f,
                23,
                12,
                List.of(),
                "37.513709",
                "127.062144",
                "더클라임",
                "01012345678",
                "instalink.com",
                "MONDAY",
                LocalDateTime.now(),
                LocalDateTime.now(),
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

    private ReviewCreateRequest createReviewCreateRequest() {
        return ReviewCreateRequest.of(
                "여기 암장 좀 괜찮네요",
                5.0f,
                1L
        );
    }

    private ReviewReactionRequest createReviewReactionRequest() {
        return ReviewReactionRequest.of(
                1
        );
    }

    private ReviewUpdateRequest createReviewUpdateRequest() {
        return ReviewUpdateRequest.of(
                "update review content",
                4.0f
        );
    }

    private static ReviewReactionPK createReviewReactionPK(
            Long userId,
            Long reviewId

    ) {
        return ReviewReactionPK.of(
                userId,
                reviewId
        );
    }

    private MultipartFile createMockMultipartFile(String image, String imageFile) throws IOException {
        return new MockMultipartFile(image, imageFile, "text/plain", imageFile.getBytes());
    }

}