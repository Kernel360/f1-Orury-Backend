package org.orury.client.review.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.ReviewErrorCode;
import org.orury.common.error.code.ReviewReactionErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.domain.ReviewReader;
import org.orury.domain.review.domain.ReviewStore;
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

import static org.orury.common.util.S3Folder.REVIEW;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewReader reviewReader;
    private final ReviewStore reviewStore;
    private final GymStore gymStore;
    private final ImageStore imageStore;

    private static final int minReactionType = 1;
    private static final int maxReactionType = 5;

    @Transactional
    @Override
    public void createReview(ReviewDto reviewDto, List<MultipartFile> files) {
        isExist(reviewDto.userDto(), reviewDto.gymDto());
        var images = imageStore.upload(REVIEW, files);
        reviewStore.save(reviewDto.toEntity(images));
        gymStore.increaseReviewCountAndTotalScore(reviewDto.gymDto().id(), reviewDto.score());
    }

    @Override
    public ReviewDto getReviewDtoById(Long reviewId, Long userId) {
        Review review = reviewReader.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.NOT_FOUND));
        isValidate(review.getUser().getId(), userId);
        return ReviewDto.from(review);
    }

    @Transactional
    @Override
    public void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, List<MultipartFile> files) {
        gymStore.updateTotalScore(beforeReviewDto.id(), beforeReviewDto.score(), updateReviewDto.score());
        var images = imageStore.upload(REVIEW, files);
        reviewStore.save(updateReviewDto.toEntity(images));
        imageStore.delete(REVIEW, beforeReviewDto.images());
    }

    @Transactional
    @Override
    public void deleteReview(ReviewDto reviewDto) {
        gymStore.decreaseReviewCountAndTotalScore(reviewDto.gymDto().id(), reviewDto.score());
        reviewStore.delete(reviewDto.toEntity());
        imageStore.delete(REVIEW, reviewDto.images());
    }

    @Override
    public List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewReader.findByGymIdOrderByIdDesc(gymId, pageable)
                : reviewReader.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);

        return convertReviewsToReviewDtos(reviews);
    }

    @Override
    public List<ReviewDto> getReviewDtosByUserId(Long userId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewReader.findByUserIdOrderByIdDesc(userId, pageable)
                : reviewReader.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);

        return convertReviewsToReviewDtos(reviews);
    }

    @Override
    public List<ReviewDto> getAllReviewDtosByGymId(Long gymId) {
        var reviews = reviewReader.findByGymId(gymId);
        return convertReviewsToReviewDtos(reviews);
    }

    @Override
    public int getReactionType(Long userId, Long reviewId) {
        ReviewReactionPK reactionPK = ReviewReactionPK.of(userId, reviewId);
        Optional<ReviewReaction> reviewReaction = reviewReader.findById(reactionPK);

        return reviewReaction.map(ReviewReaction::getReactionType).orElse(NumberConstants.NOT_REACTION);
    }

    @Transactional
    @Override
    public void processReviewReaction(ReviewReactionDto reviewReactionDto) {
        reviewReader.findById(reviewReactionDto.reviewReactionPK().getReviewId())
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.NOT_FOUND));

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

    private List<ReviewDto> convertReviewsToReviewDtos(List<Review> reviews) {
        return reviews.stream().map(ReviewDto::from).toList();
    }

    private void isValidate(Long id1, Long id2) {
        if (!Objects.equals(id1, id2)) throw new BusinessException(ReviewErrorCode.FORBIDDEN);
    }

    private void isExist(UserDto userDto, GymDto gymDto) {
        boolean exist = reviewReader.existsByUserIdAndGymId(userDto.id(), gymDto.id());
        if (exist) throw new BusinessException(ReviewErrorCode.BAD_REQUEST);
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
