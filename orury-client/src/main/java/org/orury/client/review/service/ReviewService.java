package org.orury.client.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.orury.common.error.code.ReviewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.domain.ImageUtils;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.db.model.Review;
import org.orury.domain.review.db.repository.ReviewRepository;
import org.orury.domain.review.dto.ReviewDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final GymStore gymStore;
    private final ImageUtils imageUtils;

    @Transactional
    public void createReview(ReviewDto reviewDto, List<MultipartFile> images) {
        gymStore.increaseReviewCountAndTotalScore(reviewDto.gymDto().id(), reviewDto.score());
        imageUploadAndSave(reviewDto, images);
    }

    @Transactional(readOnly = true)
    public void isExist(UserDto userDto, GymDto gymDto) {
        boolean exist = reviewRepository.existsByUserIdAndGymId(userDto.id(), gymDto.id());
        if (exist) throw new BusinessException(ReviewErrorCode.BAD_REQUEST);
    }

    @Transactional
    public void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, List<MultipartFile> images) {
        gymStore.updateTotalScore(beforeReviewDto.id(), beforeReviewDto.score(), updateReviewDto.score());
        imageUploadAndSave(updateReviewDto, images);
        imageUtils.oldS3ImagesDelete(S3Folder.REVIEW.getName(), beforeReviewDto.images());
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDtoById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.NOT_FOUND));
        var urls = imageUtils.getUrls(S3Folder.REVIEW.getName(), review.getImages());
        return ReviewDto.from(review, urls);
    }

    public void isValidate(Long id1, Long id2) {
        if (!Objects.equals(id1, id2)) throw new BusinessException(ReviewErrorCode.FORBIDDEN);
    }

    @Transactional
    public void deleteReview(ReviewDto reviewDto) {
        gymStore.decreaseReviewCountAndTotalScore(reviewDto.gymDto().id(), reviewDto.score());
        reviewRepository.delete(reviewDto.toEntity());
        imageUtils.oldS3ImagesDelete(S3Folder.REVIEW.getName(), reviewDto.images());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewRepository.findByGymIdOrderByIdDesc(gymId, pageable)
                : reviewRepository.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);

        return transferReview(reviews);
    }

    public List<ReviewDto> getReviewDtosByUserId(Long userId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewRepository.findByUserIdOrderByIdDesc(userId, pageable)
                : reviewRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);

        return transferReview(reviews);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReviewDtosByGymId(Long gymId) {
        var reviews = reviewRepository.findByGymId(gymId);
        return transferReview(reviews);
    }

    private List<ReviewDto> transferReview(List<Review> reviews) {
        return reviews.stream()
                .map(it -> {
                    var urls = imageUtils.getUrls(S3Folder.REVIEW.getName(), it.getImages());
                    return ReviewDto.from(it, urls);
                })
                .toList();
    }

    private void imageUploadAndSave(ReviewDto reviewDto, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            reviewRepository.save(reviewDto.toEntity(List.of()));
        } else {
            List<String> images = imageUtils.upload(S3Folder.REVIEW.getName(), files);
            reviewRepository.save(reviewDto.toEntity(images));
        }
    }
}
