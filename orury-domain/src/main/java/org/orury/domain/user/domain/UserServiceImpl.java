package org.orury.domain.user.domain;

import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.ImageUrlConverter;
import org.orury.common.util.S3Folder;
import org.orury.domain.comment.infrastructure.CommentLikeRepository;
import org.orury.domain.comment.infrastructure.CommentRepository;
import org.orury.domain.global.domain.ImageUtils;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.review.db.repository.ReviewReactionRepository;
import org.orury.domain.review.db.repository.ReviewRepository;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final UserStore userStore;
    private final ImageUtils imageUtils;
    private final PostStore postStore;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final GymStore gymStore;

    @Override
    public UserDto getUserDtoById(Long id) {
        User user = userReader.findUserById(id);
        String profileUrl = imageUtils.getUserImageUrl(user.getProfileImage());
        return UserDto.from(user, profileUrl);
    }

    @Override
    @Transactional
    public void updateProfileImage(UserDto userDto, MultipartFile image) throws BusinessException {
        //기존에 저장된 요소 삭제
        imageUtils.oldS3ImagesDelete(S3Folder.USER.getName(), userDto.profileImage());

        imageUploadAndSave(userDto, image);
    }

    @Override
    @Transactional
    public void updateUserInfo(UserDto userDto) {
        userStore.save(userDto.toEntity(ImageUrlConverter.splitUrlToImage(userDto.profileImage())));
    }

    @Override
    @Transactional
    public void deleteUser(UserDto userDto) {
        deleteReviewReactionsByUserId(userDto.id());
        gymStore.deleteGymLikesByUserId(userDto.id());
        deleteCommentLikesByUserId(userDto.id());
        postStore.deletePostLikesByUserId(userDto.id());
        postStore.deletePostsByUserId(userDto.id());

        imageUtils.oldS3ImagesDelete(S3Folder.USER.getName(), userDto.profileImage());
        var deletingUser = userDto.toEntity().delete(imageUtils.getUserDefaultImage());

        userStore.save(deletingUser);
    }

    private void deleteReviewReactionsByUserId(Long userId) {
        reviewReactionRepository.findByReviewReactionPK_UserId(userId)
                .forEach(
                        reviewReaction -> {
                            reviewRepository.decreaseReactionCount(reviewReaction.getReviewReactionPK()
                                    .getReviewId(), reviewReaction.getReactionType());
                            reviewReactionRepository.delete(reviewReaction);
                        }
                );
    }

    private void deleteCommentLikesByUserId(Long userId) {
        commentLikeRepository.findByCommentLikePK_UserId(userId)
                .forEach(
                        commentLike -> {
                            commentRepository.decreaseLikeCount(commentLike.getCommentLikePK()
                                    .getCommentId());
                            commentLikeRepository.delete(commentLike);
                        }
                );
    }

    private void imageUploadAndSave(UserDto userDto, MultipartFile file) {
        //MultipartFile이 오지 않은 경우 -> 유저의 프로필 이미지를 기본 이미지로 변경
        if (file == null || file.isEmpty()) {
            userStore.save(userDto.toEntity(imageUtils.getUserDefaultImage()));
        } else {
            //MultipartFile이 오는 경우 -> 유저의 프로필 이미지를 업로드한 이미지로 변경
            String image = imageUtils.upload(S3Folder.USER.getName(), file);
            userStore.save(userDto.toEntity(image));
        }
    }
}
