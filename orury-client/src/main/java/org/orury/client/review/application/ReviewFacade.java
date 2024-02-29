package org.orury.client.review.application;

import lombok.RequiredArgsConstructor;
import org.orury.client.gym.application.GymService;
import org.orury.client.review.interfaces.request.ReviewCreateRequest;
import org.orury.client.review.interfaces.request.ReviewReactionRequest;
import org.orury.client.review.interfaces.request.ReviewUpdateRequest;
import org.orury.client.review.interfaces.response.ReviewsResponse;
import org.orury.client.review.interfaces.response.ReviewsWithCursorResponse;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.orury.domain.user.domain.UserService;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewFacade {
    private final ReviewService reviewService;
    private final UserService userService;
    private final GymService gymService;

    public void createReview(Long userId, ReviewCreateRequest request, List<MultipartFile> image) {
        UserDto userDto = userService.getUserDtoById(userId);
        GymDto gymDto = gymService.getGymDtoById(request.gymId());

        ReviewDto reviewDto = request.toDto(userDto, gymDto);

        reviewService.createReview(reviewDto, image);
    }

    public void updateReview(Long reviewId, Long userId, ReviewUpdateRequest request, List<MultipartFile> image) {
        ReviewDto beforeReviewDto = reviewService.getReviewDtoById(reviewId, userId);
        ReviewDto updateReviewDto = request.toDto(beforeReviewDto);

        reviewService.updateReview(beforeReviewDto, updateReviewDto, image);
    }

    public void deleteReview(Long reviewId, Long userId) {
        ReviewDto reviewDto = reviewService.getReviewDtoById(reviewId, userId);

        reviewService.deleteReview(reviewDto);
    }

    public ReviewsWithCursorResponse getGymReviews(Long gymId, Long userId, Long cursor) {
        String gymName = gymService.getGymDtoById(gymId).name();
        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByGymId(gymId, cursor, PageRequest.of(0, NumberConstants.REVIEW_PAGINATION_SIZE));
        List<ReviewsResponse> reviewsResponses = reviewDtos.stream()
                .map(reviewDto -> {
                    int myReaction = reviewService.getReactionType(userId, reviewDto.id());
                    return ReviewsResponse.of(reviewDto, userId, myReaction);
                })
                .toList();
        return ReviewsWithCursorResponse.of(reviewsResponses, gymName);
    }

    public void processReviewReaction(Long reviewId, ReviewReactionRequest request, Long userId) {
        ReviewReactionPK reactionPK = ReviewReactionPK.of(userId, reviewId);
        ReviewReactionDto reviewReactionDto = ReviewReactionDto.of(reactionPK, request.reactionType());

        reviewService.processReviewReaction(reviewReactionDto);
    }
}
