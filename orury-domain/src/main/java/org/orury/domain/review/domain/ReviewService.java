package org.orury.domain.review.domain;

import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    public void createReview(ReviewDto reviewDto, List<MultipartFile> images);

    public void isExist(UserDto userDto, GymDto gymDto);

    public void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, List<MultipartFile> images);

    public ReviewDto getReviewDtoById(Long id);

    public void isValidate(Long id1, Long id2);

    public void deleteReview(ReviewDto reviewDto);

    public List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable);

    public List<ReviewDto> getReviewDtosByUserId(Long userId, Long cursor, Pageable pageable);

    public List<ReviewDto> getAllReviewDtosByGymId(Long gymId);
//    private List<ReviewDto> transferReview(List<Review> reviews);
//
//    private void imageUploadAndSave(ReviewDto reviewDto, List<MultipartFile> files);
}
