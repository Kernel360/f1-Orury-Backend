package org.fastcampus.oruryclient.user.service;

import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurycommon.util.ImageUrlConverter;
import org.fastcampus.orurycommon.util.ImageUtils;
import org.fastcampus.orurycommon.util.S3Folder;
import org.fastcampus.orurydomain.comment.db.model.Comment;
import org.fastcampus.orurydomain.comment.db.model.CommentLike;
import org.fastcampus.orurydomain.comment.db.model.CommentLikePK;
import org.fastcampus.orurydomain.comment.db.repository.CommentLikeRepository;
import org.fastcampus.orurydomain.comment.db.repository.CommentRepository;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.gym.db.model.Gym;
import org.fastcampus.orurydomain.gym.db.model.GymLike;
import org.fastcampus.orurydomain.gym.db.model.GymLikePK;
import org.fastcampus.orurydomain.gym.db.repository.GymLikeRepository;
import org.fastcampus.orurydomain.gym.db.repository.GymRepository;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.post.db.model.PostLike;
import org.fastcampus.orurydomain.post.db.model.PostLikePK;
import org.fastcampus.orurydomain.post.db.repository.PostLikeRepository;
import org.fastcampus.orurydomain.post.db.repository.PostRepository;
import org.fastcampus.orurydomain.post.dto.PostDto;
import org.fastcampus.orurydomain.review.db.model.Review;
import org.fastcampus.orurydomain.review.db.model.ReviewReaction;
import org.fastcampus.orurydomain.review.db.model.ReviewReactionPK;
import org.fastcampus.orurydomain.review.db.repository.ReviewReactionRepository;
import org.fastcampus.orurydomain.review.db.repository.ReviewRepository;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] 유저 테스트")
@ActiveProfiles("test")
class UserServiceTest {

    private UserRepository userRepository;
    private ImageUtils imageUtils;
    private PostRepository postRepository;
    private PostLikeRepository postLikeRepository;
    private CommentRepository commentRepository;
    private CommentLikeRepository commentLikeRepository;
    private ReviewRepository reviewRepository;
    private ReviewReactionRepository reviewReactionRepository;
    private GymRepository gymRepository;
    private GymLikeRepository gymLikeRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        imageUtils = mock(ImageUtils.class);
        postRepository = mock(PostRepository.class);
        postLikeRepository = mock(PostLikeRepository.class);
        commentRepository = mock(CommentRepository.class);
        commentLikeRepository = mock(CommentLikeRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        reviewReactionRepository = mock(ReviewReactionRepository.class);
        gymRepository = mock(GymRepository.class);
        gymLikeRepository = mock(GymLikeRepository.class);

        userService = new UserService(userRepository, imageUtils, postRepository, postLikeRepository, commentRepository, commentLikeRepository, reviewRepository, reviewReactionRepository, gymRepository, gymLikeRepository);
    }

    @Test
    @DisplayName("userId를 통해 UserDto를 반환한다.")
    void when_UserId_Then_ReturnUserDtoSuccessfully() {
        // given
        UserDto userDto = createUserDto(1L);
        User user = createUser(1L);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(imageUtils.getUserImageUrl(anyString())).willReturn("test.png");

        // when

        UserDto actualUserDto = userService.getUserDtoById(anyLong());

        // then
        then(userRepository).should(times(1)).findById(anyLong());
        then(imageUtils).should().getUserImageUrl(anyString());

        assertThat(actualUserDto).isEqualTo(userDto);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 id를 입력하면 NOT_FOUND 예외를 던진다.")
    void when_NotExistUserId_Then_ThrowException() {
        // given
        Long notExistsUserId = -1L;

        // when & then
        BusinessException exception = Assertions.assertThrows(BusinessException.class,
                () -> userService.getUserDtoById(notExistsUserId));

        assertThat(exception.getStatus()).isEqualTo(UserErrorCode.NOT_FOUND.getStatus());
    }

    @Test
    @DisplayName("유저의 프로필 이미지를 업데이트한다.")
    void when_UpdateUserProfileImage_Then_UpdateSuccessfully() throws BusinessException {
        // given
        UserDto userDto = createUserDto(1L);
        MultipartFile file = mock(MultipartFile.class);
        String image = imageUtils.upload(S3Folder.USER.getName(), file);


        // when
        userService.updateProfileImage(userDto, file);

        // then
        then(imageUtils).should(times(1)).oldS3ImagesDelete(anyString(), anyString());
        then(userRepository).should(times(1)).save(userDto.toEntity(image));
    }

    @Test
    @DisplayName("MultipartFile이 null이거나 빈 파일인 경우 기본 프로필 이미지로 업데이트한다.")
    void when_UpdateUserProfileImageWithNullOrEmpty_Then_UpdateSuccessfullyAsDefaultProfileImage() throws BusinessException {
        // given
        UserDto userDto = createUserDto(1L);
        MultipartFile file = null;
        MultipartFile emptyFile = mock(MultipartFile.class);
        ImageUtils imageUtils = mock(ImageUtils.class);
        given(emptyFile.isEmpty()).willReturn(true);

        // when
        userService.updateProfileImage(userDto, file);
        userService.updateProfileImage(userDto, emptyFile);

        // then
        then(userRepository).should(times(2)).save(userDto.toEntity(imageUtils.getUserDefaultImage()));
    }

    @Test
    @DisplayName("유저의 정보를 업데이트한다.")
    void when_UpdateUserInfo_Then_UpdateSuccessfully() {
        // given
        UserDto userDto = createUserDto(1L);

        // when
        userService.updateUserInfo(userDto);

        // then
        then(userRepository).should(times(1)).save(userDto.toEntity(ImageUrlConverter.splitUrlToImage(userDto.profileImage())));
    }

    @Test
    @DisplayName("존재하는 유저를 탈퇴처리하면 유저 탈퇴 작업을 진행한다.")
    void when_DeleteExistUser_Then_UpdateIsDeletedColumn() {
        // given
        UserDto userDto = createDeletedUserDto(1L);
        Gym gym = createGym(1L);
        GymLike gymLike = createGymLike(userDto.id(), gym.getId());
        Review review = createReview(1L, userDto.id(), gym.getId());
        ReviewReaction reviewReaction = createReviewReaction(userDto.id(), review.getId());
        Comment comment = createComment(userDto.toEntity());
        CommentLike commentLike = createCommentLike(userDto.id(), comment.getId());
        Post post = createPost(1L, userDto.id());
        PostLike postLike = createPostLike(userDto.id(), post.getId());

        given(reviewReactionRepository.findByReviewReactionPK_UserId(userDto.id())).willReturn(Arrays.asList(reviewReaction));
        given(gymLikeRepository.findByGymLikePK_UserId(userDto.id())).willReturn(Arrays.asList(gymLike));
        given(commentLikeRepository.findByCommentLikePK_UserId(userDto.id())).willReturn(Arrays.asList(commentLike));
        given(postLikeRepository.findByPostLikePK_UserId(userDto.id())).willReturn(Arrays.asList(postLike));
        given(postRepository.findByUser_Id(userDto.id())).willReturn(Arrays.asList(post));

        // when
        userService.deleteUser(userDto);

        // then
        then(imageUtils).should(times(1)).oldS3ImagesDelete(anyString(), anyString());
        then(userRepository).should(times(1)).save(userDto.toEntity());

        then(reviewReactionRepository).should(times(1)).findByReviewReactionPK_UserId(userDto.id());
        then(reviewRepository).should(atLeastOnce()).decreaseReactionCount(reviewReaction.getReviewReactionPK().getReviewId(),
                reviewReaction.getReactionType());
        then(reviewReactionRepository).should(atLeastOnce()).delete(reviewReaction);

        then(gymLikeRepository).should(times(1)).findByGymLikePK_UserId(userDto.id());
        then(gymRepository).should(atLeastOnce()).decreaseLikeCount(gymLike.getGymLikePK().getGymId());
        then(gymLikeRepository).should(atLeastOnce()).delete(gymLike);

        then(commentLikeRepository).should(times(1)).findByCommentLikePK_UserId(userDto.id());
        then(commentRepository).should(atLeastOnce()).decreaseLikeCount(commentLike.getCommentLikePK().getCommentId());
        then(commentLikeRepository).should(atLeastOnce()).delete(commentLike);

        then(postLikeRepository).should(times(1)).findByPostLikePK_UserId(userDto.id());
        then(postRepository).should(atLeastOnce()).decreaseCommentCount(postLike.getPostLikePK().getPostId());
        then(postLikeRepository).should(atLeastOnce()).delete(postLike);

        then(postRepository).should(times(1)).findByUser_Id(userDto.id());
        then(imageUtils).should(atLeastOnce()).oldS3ImagesDelete(S3Folder.POST.getName(), post.getImages());
        then(postRepository).should(atLeastOnce()).delete(post);
    }


    private static UserDto createUserDto(Long id) {
        return UserDto.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "test.png",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private static User createUser(Long id) {
        return User.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private static UserDto createDeletedUserDto(Long id) {
        return UserDto.of(
                id,
                "userEmail",
                "userNickname",
                "userPassword",
                1,
                1,
                LocalDate.now(),
                "test.png",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                NumberConstants.IS_DELETED
        );
    }

    public static List<MultipartFile> createMultiFiles() {
        try {
            // 여러 개의 MultipartFile을 생성하여 배열에 담아 반환
            return List.of(
                    createMockMultipartFile("key1", "image.png"),
                    createMockMultipartFile("key2", "image2.png")
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to create MultipartFile.", e);
        }
    }

    private static MultipartFile createMockMultipartFile(String image, String imageFile) throws IOException {
        return new MockMultipartFile(image, imageFile, "text/plain", imageFile.getBytes());
    }

    private static Review createReview(Long reviewId, Long userId, Long gymId) {
        return Review.of(
                reviewId,
                "reviewContent",
                List.of(),
                4.5f,
                0,
                1,
                2,
                3,
                4,
                createUser(userId),
                createGym(gymId),
                null,
                null
        );
    }

    private static ReviewReaction createReviewReaction(Long userId, Long reviewId) {
        ReviewReactionPK reviewReactionPK = ReviewReactionPK.of(userId, reviewId);
        return ReviewReaction.of(reviewReactionPK, 1);
    }

    private static Gym createGym(Long id) {
        return Gym.of(
                id,
                "gymName",
                "gymKakaoId",
                "gymRoadAddress",
                "gymAddress",
                40.5f,
                23,
                12,
                List.of(),
                "123.456",
                "123.456",
                "gymBrand",
                "010-1234-5678",
                "gymInstaLink",
                "MONDAY",
                "11:00-23:11",
                "12:00-23:22",
                "13:00-23:33",
                "14:00-23:44",
                "15:00-23:55",
                "16:00-23:66",
                "17:00-23:77",
                "gymHomepageLink",
                "gymRemark"
        );
    }

    private static GymLike createGymLike(Long userId, Long gymId) {
        GymLikePK gymLikePK = GymLikePK.of(userId, gymId);
        return GymLike.of(gymLikePK);
    }

    private static PostDto createPostDto(Long postId, Long userId) {
        return PostDto.of(
                1L,
                "title",
                "content",
                0,
                0,
                0,
                List.of("post1.png", "post2.png"),
                1,
                createUserDto(userId),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static Post createPost(Long postId, Long userId) {
        return Post.of(
                postId,
                "title",
                "content",
                0,
                0,
                0,
                List.of("post1.png", "post2.png"),
                1,
                createPostDto(postId, userId).userDto()
                        .toEntity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static PostLike createPostLike(Long userId, Long postId) {
        PostLikePK postLikePK = PostLikePK.of(1L, 1L);
        return PostLike.of(postLikePK);
    }

    private static Comment createComment(User user) {
        return Comment.of(
                1L,
                "commentContent",
                NumberConstants.PARENT_COMMENT,
                0,
                createPost(1L, user.getId()),
                user,
                NumberConstants.IS_NOT_DELETED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private static CommentLike createCommentLike(Long userId, Long commentId) {
        CommentLikePK commentLikePK = CommentLikePK.of(userId, commentId);
        return CommentLike.of(commentLikePK);
    }

}