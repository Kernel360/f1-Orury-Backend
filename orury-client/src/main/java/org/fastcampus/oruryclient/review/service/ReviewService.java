package org.fastcampus.oruryclient.review.service;

import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.orurycommon.error.code.ReviewErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurycommon.util.S3Folder;
import org.fastcampus.orurycommon.util.S3Repository;
import org.fastcampus.orurydomain.gym.db.repository.GymRepository;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.fastcampus.orurydomain.review.db.model.Review;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.review.dto.ReviewDto;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final GymRepository gymRepository;
    private final S3Repository s3Repository;

    @Transactional
    public void createReview(ReviewDto reviewDto, MultipartFile... images) {
        gymRepository.increaseReviewCount(reviewDto.gymDto().id());
        gymRepository.sumTotalScore(reviewDto.gymDto().id(), reviewDto.score());
        imageUploadAndSave(reviewDto, images);
    }

    @Transactional(readOnly = true)
    public void isExist(UserDto userDto, GymDto gymDto) {
        boolean exist = reviewRepository.existsByUser_IdAndGym_Id(userDto.id(), gymDto.id());
        if (exist) throw new BusinessException(ReviewErrorCode.BAD_REQUEST);
    }

    @Transactional
    public void updateReview(ReviewDto beforeReviewDto, ReviewDto updateReviewDto, MultipartFile... images) {
        oldS3ImagesDelete(updateReviewDto);
        gymRepository.subtractTotalScore(beforeReviewDto.gymDto().id(), beforeReviewDto.score());
        gymRepository.sumTotalScore(updateReviewDto.gymDto().id(), updateReviewDto.score());
        imageUploadAndSave(updateReviewDto, images);
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDtoById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.NOT_FOUND));
        var urls = s3Repository.getUrls(S3Folder.REVIEW.getName(), ImageUrlConverter.splitUrlToImage(review.getImages()));
        var images = ImageUrlConverter.convertListToString(urls);
        return ReviewDto.from(review, images);
    }

    public void isValidate(Long id1, Long id2) {
        if (!Objects.equals(id1, id2)) throw new BusinessException(ReviewErrorCode.FORBIDDEN);
    }

    @Transactional
    public void deleteReview(ReviewDto reviewDto) {
        oldS3ImagesDelete(reviewDto);
        gymRepository.decreaseReviewCount(reviewDto.gymDto().id());
        gymRepository.subtractTotalScore(reviewDto.gymDto().id(), reviewDto.score());
        reviewRepository.delete(reviewDto.toEntity());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewDtosByGymId(Long gymId, Long cursor, Pageable pageable) {
        List<Review> reviews = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? reviewRepository.findByGymIdOrderByIdDesc(gymId, pageable)
                : reviewRepository.findByGymIdAndIdLessThanOrderByIdDesc(gymId, cursor, pageable);

        return reviews.stream()
                .map(ReviewDto::from)
                .toList();
    }

    private void imageUploadAndSave(ReviewDto reviewDto, MultipartFile... images) {
        if (s3Repository.isEmpty(images)) {
            reviewRepository.save(reviewDto.toEntity(null));
        } else {
            List<String> imageUrls = s3Repository.upload(S3Folder.REVIEW.getName(), images);
            String convertUrl = ImageUrlConverter.convertListToString(imageUrls);
            reviewRepository.save(reviewDto.toEntity(convertUrl));
        }
    }

    private void oldS3ImagesDelete(ReviewDto reviewDto) {
        String[] oldImages = reviewDto.images()
                .split(",");
        s3Repository.delete(S3Folder.REVIEW.getName(), oldImages);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReviewDtosByGymId(Long gymId) {
        return reviewRepository.findByGymId(gymId)
                .stream().map(ReviewDto::from).toList();
    }
}
