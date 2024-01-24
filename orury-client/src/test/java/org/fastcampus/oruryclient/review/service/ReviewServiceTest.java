package org.fastcampus.oruryclient.review.service;

import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.orurycommon.error.code.ReviewErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurycommon.util.S3Repository;
import org.fastcampus.orurydomain.gym.db.model.Gym;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.db.model.Review;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 리뷰 Service 테스트")
@ActiveProfiles("test")
class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        S3Repository s3Repository = mock(S3Repository.class);
        reviewService = new ReviewService(reviewRepository, s3Repository);
    }

    @Test
    @DisplayName("리뷰가 생성하기 위해 repository의 save 메소드를 성공적으로 호출한다.")
    void should_ReviewCreatedSuccessfully() {
        //given
        ReviewDto reviewDto = ReviewDto.from(createReview(1L));

        //when
        reviewService.createReview(reviewDto);

        //then
        then(reviewRepository).should()
                .save(any());
    }

    @Test
    @DisplayName("동일한 userId, gymId로 작성된 리뷰가 없다면, 아무런 값도 return 하지 않는다.")
    void when_NotExistingUserIdAndGymId_Then_NotReturn() {
        //given
        UserDto userDto = UserDto.from(createUser(1L));
        GymDto gymDto = GymDto.from(createGym(1L));

        given(reviewRepository.existsByUser_IdAndGym_Id(userDto.id(), gymDto.id())).willReturn(false);

        //when
        reviewService.isExist(userDto, gymDto);

        //then
        then(reviewRepository).should()
                .existsByUser_IdAndGym_Id(any(), any());
    }

    @Test
    @DisplayName("동일한 userId, gymId로 작성된 리뷰가 있다면, BAD_REQUEST 예외를 발생시킨다.")
    void when_ExistingUserIdAndGymId_Then_BadRequestException() {
        //given
        UserDto userDto = UserDto.from(createUser(1L));
        GymDto gymDto = GymDto.from(createGym(1L));

        given(reviewRepository.existsByUser_IdAndGym_Id(userDto.id(), gymDto.id())).willReturn(true);

        //when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.isExist(userDto, gymDto));
        assertEquals(ReviewErrorCode.BAD_REQUEST.getStatus(), exception.getStatus());

        then(reviewRepository).should()
                .existsByUser_IdAndGym_Id(any(), any());
    }

    @Test
    @DisplayName("존재하는 리뷰를 성공적으로 수정해야 한다.")
    void should_ReviewUpdatedSuccessfully() {
        //given
        ReviewDto reviewDto = ReviewDto.from(createReview(1L));

        //when
        reviewService.createReview(reviewDto);

        //then
        then(reviewRepository).should()
                .save(any());

    }

    @Test
    @DisplayName("리뷰 id에 해당하는 ReviewDto를 반환해야 한다.")
    void should_GetReviewByid() {
        //given
        Review review = createReview(1L);
        given(reviewRepository.findById(anyLong())).willReturn(Optional.of(review));

        //when
        ReviewDto reviewDto = reviewService.getReviewDtoById(anyLong());

        //then
        then(reviewRepository).should()
                .findById(anyLong());

        assertEquals(ReviewDto.from(review), createReviewDto(1L));
    }

    @Test
    @DisplayName("존재하지 않는 리뷰를 조회하면 NOT FOUND 예외를 던진다.")
    void when_GetNotExistReview_Then_NotFoundException() {
        //given
        given(reviewRepository.findById(anyLong())).willReturn(Optional.empty());

        //when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.getReviewDtoById(1L));
        assertEquals(ReviewErrorCode.NOT_FOUND.getStatus(), exception.getStatus());

        then(reviewRepository).should()
                .findById(anyLong());
    }

    @Test
    @DisplayName("리뷰 수정 시, 리뷰를 작성한 유저 ID와 수정을 요청한 유저 ID값이 다르면 FORBIDDEN 예외를 던진다.")
    void when_UpdateReviewNotEqualUserId_Then_ForbiddenException() {
        //given
        Long originUserId = 1L;
        Long requestUserId = 2L;

        //when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> reviewService.isValidate(originUserId, requestUserId));
        assertEquals(ReviewErrorCode.FORBIDDEN.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("존재하는 리뷰가 성공적으로 삭제되어야 한다.")
    void should_ReviewDeletedSuccessfully() {
        //given
        ReviewDto reviewDto = ReviewDto.from(createReview(1L));

        //when
        reviewService.deleteReview(reviewDto);

        //then
        then(reviewRepository).should()
                .delete(any());
    }

    @Test
    @DisplayName("첫번째 커서와 암장 ID를 입력받아 암장에 대한 리뷰를 paging 처리하여 List<ReviewDto>로 반환한다.")
    void when_InputFirstCursorAndGymId_Then_ShowReviewOfGymAsPaging() {
        //given
        Long gymId = 1L;
        Long cursor = 0L;
        PageRequest pageRequest = PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE);
        List<Review> reviews = createReviewsAboutGym();

        given(reviewRepository.findByGymIdOrderByIdDesc(anyLong(), any())).willReturn(reviews);

        //when
        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByGymId(gymId, cursor, pageRequest);

        //then
        assertEquals(reviewDtos, reviews.stream()
                .map(ReviewDto::from)
                .toList());
        then(reviewRepository).should()
                .findByGymIdOrderByIdDesc(anyLong(), any());
    }

    private static User createUser(Long id) {
        return User.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                null,
                "userProfileImage",
                null,
                null
        );
    }

    private static Gym createGym(Long id) {
        return Gym.of(
                id,
                "gymName",
                "gymKakaoId",
                "gymRoadAddress",
                "gymAddress",
                4.5f,
                12,
                "gymImages",
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

    private static Review createReview(Long id) {
        return Review.of(
                id,
                "reviewContent",
                "reviewImages",
                4.5f,
                0,
                1,
                2,
                3,
                4,
                createUser(id),
                createGym(id),
                null,
                null
        );
    }

    private static Review createReviewAboutGym(Long id) {
        return Review.of(
                id,
                "reviewContent",
                "reviewImages",
                4.5f,
                0,
                1,
                2,
                3,
                4,
                createUser(id),
                createGym(1L),
                null,
                null
        );
    }

    private static ReviewDto createReviewDto(Long id) {
        return ReviewDto.of(
                id,
                "reviewContent",
                "reviewImages",
                4.5f,
                0,
                1,
                2,
                3,
                4,
                UserDto.from(createUser(id)),
                GymDto.from(createGym(id)),
                null,
                null
        );
    }

    private static List<Review> createReviewsAboutGym() {
        List<Review> reviews = new ArrayList<>();

        for (Long l = 1L; l <= 10L; l++) {
            reviews.add(createReviewAboutGym(l));
        }

        return reviews;
    }

}
