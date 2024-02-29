package org.orury.client.review.application;

import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewDto reviewDto, List<MultipartFile> images);

    void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, List<MultipartFile> images);

    ReviewDto getReviewDtoById(Long reviewId, Long userId);

    void deleteReview(ReviewDto reviewDto);

    List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable);

    List<ReviewDto> getReviewDtosByUserId(Long userId, Long cursor, Pageable pageable);

    List<ReviewDto> getAllReviewDtosByGymId(Long gymId);

    int getReactionType(Long userId, Long reviewId);

    void processReviewReaction(ReviewReactionDto reviewReactionDto);
}
