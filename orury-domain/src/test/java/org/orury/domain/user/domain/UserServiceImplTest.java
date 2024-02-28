package org.orury.domain.user.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.comment.db.repository.CommentLikeRepository;
import org.orury.domain.comment.db.repository.CommentRepository;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.review.db.repository.ReviewReactionRepository;
import org.orury.domain.review.db.repository.ReviewRepository;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] User Test")
@ActiveProfiles("test")
class UserServiceImplTest {
    private UserReader userReader;
    private UserStore userStore;
    private ImageReader imageReader;
    private ImageStore imageStore;
    private PostStore postStore;
    private GymStore gymStore;
    private CommentRepository commentRepository;
    private CommentLikeRepository commentLikeRepository;
    private ReviewRepository reviewRepository;
    private ReviewReactionRepository reviewReactionRepository;
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        userReader = mock(UserReader.class);
        userStore = mock(UserStore.class);
        imageReader = mock(ImageReader.class);
        imageStore = mock(ImageStore.class);
        postStore = mock(PostStore.class);
        gymStore = mock(GymStore.class);
        commentRepository = mock(CommentRepository.class);
        commentLikeRepository = mock(CommentLikeRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        reviewReactionRepository = mock(ReviewReactionRepository.class);

        userServiceImpl = new UserServiceImpl(userReader, userStore, imageReader, imageStore, postStore, gymStore, commentRepository, commentLikeRepository, reviewRepository, reviewReactionRepository);
    }

    @Test
    @DisplayName("getUserDtoById(Long id) Test : userId 받아 UserDto 반환 [성공]")
    void when_UserId_Then_ReturnUserDtoSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);
        Optional<User> user = createwrappingOptional(createUser(1L));
        given(userReader.findUserById(anyLong())).willReturn(user);
        given(imageReader.getUserImageLink(anyString())).willReturn("test.png");

        //when
        UserDto actualUserDto = userServiceImpl.getUserDtoById(anyLong());

        //then
        then(userReader).should(times(1)).findUserById(anyLong());
        then(imageReader).should(times(1)).getUserImageLink(anyString());

        assertThat(actualUserDto).isEqualTo(userDto);
    }

    @Test
    @DisplayName("getUserDtoById(Long id) Test : 존재하지 않는 userId라면 when_NotExistUserId_Then_ThrowException [실패]")
    void when_NotExistUserId_Then_ThrowException() {
        //given
        Long notExistsUserId = -1L;
        Optional<User> user = createEmptyOptional();
        given(userReader.findUserById(anyLong())).willReturn(user);

        // when & then
        BusinessException exception = Assertions.assertThrows(BusinessException.class,
                () -> userServiceImpl.getUserDtoById(notExistsUserId));

        assertThat(exception.getStatus()).isEqualTo(UserErrorCode.NOT_FOUND.getStatus());
    }

    @Test
    @DisplayName("updateProfileImage(UserDto userDto, MultipartFile image) Test : 유저의 프로필 이미지를 업데이트한다. [성공]")
    void when_UpdateUserProfileImage_Then_UpdateSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);
        MultipartFile image = mock(MultipartFile.class);

        // when
        userServiceImpl.updateProfileImage(userDto, image);

        // then
        then(imageStore).should(times(1)).delete(any());
    }

    @Test
    @DisplayName("updateUserInfo(UserDto userDto) Test : UserDto로 유저정보를 업데이트한다. [성공]")
    void when_UpdateUserInfo_Then_UpdateSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);

        // when
        userServiceImpl.updateUserInfo(userDto);

        // then
        then(userStore).should(times(1)).save(any());
    }

    @Test
    @DisplayName("deleteUser(UserDto userDto) Test : UserDto로 유저를 삭제한다. [성공]")
    void when_deleteUser_Then_DeleteSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);

        // when
        userServiceImpl.deleteUser(userDto);

        // then
        then(gymStore).should(times(1)).deleteGymLikesByUserId(any());
        then(postStore).should(times(1)).deletePostLikesByUserId(any());
        then(postStore).should(times(1)).deletePostsByUserId(any());
        then(imageStore).should(times(1)).delete(any());
        then(userStore).should(times(1)).save(any());
    }

    @Test
    @DisplayName("imageUploadAndSave(UserDto userDto, MultipartFile file) Test : UserDto, file을 받아 이미지 업로드, 유저 정보 저장 [성공]")
    void when_UploadImage_Then_UploadAndSaveSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);
        MultipartFile image = mock(MultipartFile.class);

        // when
        userServiceImpl.imageUploadAndSave(userDto, image);

        // then
        then(imageStore).should(times(1)).upload(any());
        then(userStore).should(times(1)).save(any());
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

    public static Optional<User> createwrappingOptional(User user) {
        return Optional.ofNullable(user);
    }

    public static Optional<User> createEmptyOptional() {
        return Optional.empty();
    }


}