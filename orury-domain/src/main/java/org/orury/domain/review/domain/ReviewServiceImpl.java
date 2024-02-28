package org.orury.domain.review.domain;

import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {


    @Override
    public void createReview(ReviewDto reviewDto, List<MultipartFile> images) {

    }

    @Override
    public void isExist(UserDto userDto, GymDto gymDto) {

    }

    @Override
    public void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, List<MultipartFile> images) {

    }

    @Override
    public ReviewDto getReviewDtoById(Long id) {
        return null;
    }

    @Override
    public void isValidate(Long id1, Long id2) {

    }

    @Override
    public void deleteReview(ReviewDto reviewDto) {

    }

    @Override
    public List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public List<ReviewDto> getReviewDtosByUserId(Long userId, Long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public List<ReviewDto> getAllReviewDtosByGymId(Long gymId) {
        return null;
    }
}
