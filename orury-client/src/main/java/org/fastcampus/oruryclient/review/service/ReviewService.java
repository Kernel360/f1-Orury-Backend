package org.fastcampus.oruryclient.review.service;

import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.review.error.ReviewErrorCode;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.db.model.Review;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public void createReview(ReviewDto reviewDto) {
        reviewRepository.save(reviewDto.toEntity());
    }

    @Transactional(readOnly = true)
    public void isExist(UserDto userDto, GymDto gymDto) {
        boolean exist = reviewRepository.existsByUser_IdAndGym_Id(userDto.id(), gymDto.id());
        if (exist) throw new BusinessException(ReviewErrorCode.BAD_REQUEST);
    }

    @Transactional
    public void updateReview(ReviewDto reviewDto) {
        reviewRepository.save(reviewDto.toEntity());
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDtoById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.NOT_FOUND));
        return ReviewDto.from(review);
    }

    public void isValidate(Long id1, Long id2) {
        if (!Objects.equals(id1, id2)) throw new BusinessException(ReviewErrorCode.FORBIDDEN);
    }

    @Transactional
    public void deleteReview(ReviewDto reviewDto) {
        reviewRepository.delete(reviewDto.toEntity());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewRepository.findByGymIdOrderByIdDesc(gymId, pageable)
                : reviewRepository.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);

        return reviews.stream()
                .map(ReviewDto::from).toList();
    }
}
