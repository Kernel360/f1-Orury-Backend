package org.orury.domain.review.dto;

import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.review.db.model.Review;
import org.orury.domain.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Review}
 */
public record ReviewDto(
        Long id,
        String content,
        List<String> images,
        float score,
        int interestCount,
        int likeCount,
        int helpCount,
        int thumbCount,
        int angryCount,
        UserDto userDto,
        GymDto gymDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewDto of(
            Long id,
            String content,
            List<String> images,
            float score,
            int interestCount,
            int likeCount,
            int helpCount,
            int thumbCount,
            int angryCount,
            UserDto userDto,
            GymDto gymDto,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new ReviewDto(
                id,
                content,
                images,
                score,
                interestCount,
                likeCount,
                helpCount,
                thumbCount,
                angryCount,
                userDto,
                gymDto,
                createdAt,
                updatedAt
        );
    }

    public static ReviewDto from(Review entity) {
        return ReviewDto.of(
                entity.getId(),
                entity.getContent(),
                entity.getImages(),
                entity.getScore(),
                entity.getInterestCount(),
                entity.getLikeCount(),
                entity.getHelpCount(),
                entity.getThumbCount(),
                entity.getAngryCount(),
                UserDto.from(entity.getUser()),
                GymDto.from(entity.getGym()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static ReviewDto from(Review entity, List<String> imgUrls) {
        return ReviewDto.of(
                entity.getId(),
                entity.getContent(),
                imgUrls,
                entity.getScore(),
                entity.getInterestCount(),
                entity.getLikeCount(),
                entity.getHelpCount(),
                entity.getThumbCount(),
                entity.getAngryCount(),
                UserDto.from(entity.getUser()),
                GymDto.from(entity.getGym()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Review toEntity() {
        return Review.of(
                id,
                content,
                images,
                score,
                interestCount,
                likeCount,
                helpCount,
                thumbCount,
                angryCount,
                userDto.toEntity(),
                gymDto.toEntity(),
                createdAt,
                updatedAt
        );
    }

    public Review toEntity(List<String> images) {
        return Review.of(
                id,
                content,
                images,
                score,
                interestCount,
                likeCount,
                helpCount,
                thumbCount,
                angryCount,
                userDto.toEntity(),
                gymDto.toEntity(),
                createdAt,
                updatedAt
        );
    }
}
