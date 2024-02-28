package org.orury.domain.review.domain;

import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewDto reviewDto, List<MultipartFile> images);

    void isExist(UserDto userDto, GymDto gymDto);

    void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, List<MultipartFile> images);

    ReviewDto getReviewDtoById(Long id);

    void isValidate(Long id1, Long id2);

    void deleteReview(ReviewDto reviewDto);

    List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable);

    List<ReviewDto> getReviewDtosByUserId(Long userId, Long cursor, Pageable pageable);

    List<ReviewDto> getAllReviewDtosByGymId(Long gymId);

    public int getReactionType(Long userId, Long reviewId);

    public void processReviewReaction(ReviewReactionDto reviewReactionDto);

//    private List<ReviewDto> transferReview(List<Review> reviews);
//
//    private void imageUploadAndSave(ReviewDto reviewDto, List<MultipartFile> files);
}
