package org.orury.domain.review.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.ReviewErrorCode;
import org.orury.common.error.code.ReviewReactionErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.domain.ImageUtils;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.review.domain.dto.ReviewReactionDto;
import org.orury.domain.review.domain.entity.Review;
import org.orury.domain.review.domain.entity.ReviewReaction;
import org.orury.domain.review.domain.entity.ReviewReactionPK;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewReader reviewReader;
    private final ReviewStore reviewStore;
    private final GymStore gymStore;
    private final ImageUtils imageUtils;

    private static final int minReactionType = 1;
    private static final int maxReactionType = 5;

    @Override
    public void createReview(ReviewDto reviewDto, List<MultipartFile> images) {
        gymStore.increaseReviewCountAndTotalScore(reviewDto.gymDto().id(), reviewDto.score());
        imageUploadAndSave(reviewDto, images);
    }

    @Override
    public void isExist(UserDto userDto, GymDto gymDto) {
        boolean exist = reviewReader.existsByUserIdAndGymId(userDto.id(), gymDto.id());
        if (exist) throw new BusinessException(ReviewErrorCode.BAD_REQUEST);
    }

    @Override
    public void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, List<MultipartFile> images) {
        gymStore.updateTotalScore(beforeReviewDto.id(), beforeReviewDto.score(), updateReviewDto.score());
        imageUploadAndSave(updateReviewDto, images);
        imageUtils.oldS3ImagesDelete(S3Folder.REVIEW.getName(), beforeReviewDto.images());
    }

    @Override
    public ReviewDto getReviewDtoById(Long id) {
        Review review = reviewReader.findById(id);
        var urls = imageUtils.getUrls(S3Folder.REVIEW.getName(), review.getImages());
        return ReviewDto.from(review, urls);
    }

    @Override
    public void isValidate(Long id1, Long id2) {
        if (!Objects.equals(id1, id2)) throw new BusinessException(ReviewErrorCode.FORBIDDEN);
    }

    @Override
    public void deleteReview(ReviewDto reviewDto) {
        gymStore.decreaseReviewCountAndTotalScore(reviewDto.gymDto().id(), reviewDto.score());
        reviewStore.delete(reviewDto.toEntity());
        imageUtils.oldS3ImagesDelete(S3Folder.REVIEW.getName(), reviewDto.images());
    }

    @Override
    public List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewReader.findByGymIdOrderByIdDesc(gymId, pageable)
                : reviewReader.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);

        return transferReview(reviews);
    }

    @Override
    public List<ReviewDto> getReviewDtosByUserId(Long userId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewReader.findByUserIdOrderByIdDesc(userId, pageable)
                : reviewReader.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);

        return transferReview(reviews);
    }

    @Override
    public List<ReviewDto> getAllReviewDtosByGymId(Long gymId) {
        var reviews = reviewReader.findByGymId(gymId);
        return transferReview(reviews);
    }

    @Override
    public int getReactionType(Long userId, Long reviewId) {
        ReviewReactionPK reactionPK = ReviewReactionPK.of(userId, reviewId);
        Optional<ReviewReaction> reviewReaction = reviewReader.findById(reactionPK);

        return reviewReaction.map(ReviewReaction::getReactionType).orElse(NumberConstants.NOT_REACTION);
    }

    @Override
    public void processReviewReaction(ReviewReactionDto reviewReactionDto) {
        reviewReader.findById(reviewReactionDto.reviewReactionPK().getReviewId());

        int reactionTypeInput = reviewReactionDto.reactionType();
        if (reactionTypeInput < minReactionType || reactionTypeInput > maxReactionType) {
            throw new BusinessException(ReviewReactionErrorCode.BAD_REQUEST);
        }

        Optional<ReviewReaction> originReaction = reviewReader.findById(reviewReactionDto.reviewReactionPK());

        if (originReaction.isEmpty()) {
            createReviewReaction(reviewReactionDto);
        } else if (originReaction.get().getReactionType() != reactionTypeInput) {
            updateReviewReaction(reviewReactionDto, originReaction.get().getReactionType());
        } else {
            deleteReviewReaction(reviewReactionDto);
        }
    }

    private void imageUploadAndSave(ReviewDto reviewDto, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            reviewStore.save(reviewDto.toEntity(List.of()));
        } else {
            List<String> images = imageUtils.upload(S3Folder.REVIEW.getName(), files);
            reviewStore.save(reviewDto.toEntity(images));
        }
    }

    private List<ReviewDto> transferReview(List<Review> reviews) {
        return reviews.stream()
                .map(it -> {
                    var urls = imageUtils.getUrls(S3Folder.REVIEW.getName(), it.getImages());
                    return ReviewDto.from(it, urls);
                })
                .toList();
    }

    private void createReviewReaction(ReviewReactionDto reviewReactionDto) {
        reviewStore.increaseReactionCount(reviewReactionDto.reviewReactionPK().getReviewId(), reviewReactionDto.reactionType());
        reviewStore.save(reviewReactionDto.toEntity());
    }

    private void updateReviewReaction(ReviewReactionDto reviewReactionDto, int oldReactionType) {
        reviewStore.updateReactionCount(reviewReactionDto.reviewReactionPK().getReviewId(), oldReactionType, reviewReactionDto.reactionType());
        reviewStore.save(reviewReactionDto.toEntity());
    }

    private void deleteReviewReaction(ReviewReactionDto reviewReactionDto) {
        reviewStore.decreaseReactionCount(reviewReactionDto.reviewReactionPK().getReviewId(), reviewReactionDto.reactionType());
        reviewStore.delete(reviewReactionDto.toEntity());
    }
}
