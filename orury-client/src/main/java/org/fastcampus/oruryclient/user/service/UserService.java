package org.fastcampus.oruryclient.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurycommon.util.S3Folder;
import org.fastcampus.orurycommon.util.S3Repository;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.gym.db.repository.GymLikeRepository;
import org.fastcampus.orurydomain.gym.db.repository.GymRepository;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.post.db.repository.PostLikeRepository;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.review.db.repository.ReviewReactionRepository;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Repository s3Repository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final GymRepository gymRepository;
    private final GymLikeRepository gymLikeRepository;

    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;

    public UserDto getUserDtoById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND));
        String imgUrl = s3Repository.getUrls(S3Folder.USER.getName(), user.getProfileImage())
                .get(0);
        return UserDto.from(user, imgUrl);
    }

    @Transactional
    public void updateProfileImage(UserDto userDto, MultipartFile image) throws BusinessException {
        //기존에 저장된 요소 삭제
        s3Repository.delete(S3Folder.USER.getName(), userDto.profileImage());
        var url = s3Repository.upload(S3Folder.USER.getName(), image);

        //MultipartFile이 오지 않은 경우 -> 유저의 프로필 이미지를 기본 이미지로 변경
        //MultipartFile이 오는 경우 -> 유저의 프로필 이미지를 업로드한 이미지로 변경
        String newProfileImage = url.isEmpty() ? defaultImage : url.get(0);
        userRepository.save(userDto.toEntity(newProfileImage));
    }

    @Transactional
    public void updateUserInfo(UserDto userDto) {
        userRepository.save(userDto.toEntity());
    }

    @Transactional
    public void deleteUser(UserDto userDto) {
        deleteReviewReactionsByUserId(userDto.id());
        deleteReviewsByUserId(userDto.id());
        deleteGymLikesByUserId(userDto.id());
        deleteCommentLikesByUserId(userDto.id());
        deletePostLikesByUserId(userDto.id());
        deletePostsByUserId(userDto.id());

        s3Repository.delete(S3Folder.USER.getName(), userDto.profileImage());

        User user = User.of(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                userDto.password(),
                userDto.signUpType(),
                userDto.gender(),
                userDto.birthday(),
                defaultImage,
                userDto.createdAt(),
                null,
                NumberConstants.IS_DELETED
        );

        userRepository.save(user);
    }


    private void deleteReviewReactionsByUserId(Long userId) {
        reviewReactionRepository.findByReviewReactionPK_UserId(userId).forEach(
                reviewReaction -> {
                    reviewRepository.decreaseReactionCount(reviewReaction.getReviewReactionPK().getReviewId(), reviewReaction.getReactionType());
                    reviewReactionRepository.delete(reviewReaction);
                }
        );
    }

    private void deleteReviewsByUserId(Long userId) {
        reviewRepository.deleteAll(reviewRepository.findByUser_Id(userId));
    }

    private void deleteGymLikesByUserId(Long userId) {
        gymLikeRepository.findByGymLikePK_UserId(userId).forEach(
                gymLike -> {
                    gymRepository.decreaseLikeCount(gymLike.getGymLikePK().getGymId());
                    gymLikeRepository.delete(gymLike);
                }
        );
    }

    private void deleteCommentLikesByUserId(Long userId) {
        commentLikeRepository.findByCommentLikePK_UserId(userId).forEach(
                commentLike -> {
                    commentRepository.decreaseLikeCount(commentLike.getCommentLikePK().getCommentId());
                    commentLikeRepository.delete(commentLike);
                }
        );
    }

    private void deletePostLikesByUserId(Long userId) {
        postLikeRepository.findByPostLikePK_UserId(userId).forEach(
                postLike -> {
                    postRepository.decreaseCommentCount(postLike.getPostLikePK().getPostId());
                    postLikeRepository.delete(postLike);
                }
        );
    }

    private void deletePostsByUserId(Long userId) {
        postRepository.findByUser_Id(userId).forEach(
                post -> {
                    oldS3ImagesDelete(post);
                    postRepository.delete(post);
                }
        );
    }

    private void oldS3ImagesDelete(Post post) {
        String oldImages = post.getImages();
        if (oldImages == null) return;

        s3Repository.delete(S3Folder.POST.getName(), oldImages.split(","));
    }
}
