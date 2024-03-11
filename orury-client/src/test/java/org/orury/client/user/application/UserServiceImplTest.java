package org.orury.client.user.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.S3Folder;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.review.domain.ReviewStore;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] User Test")
@ActiveProfiles("test")
class UserServiceImplTest {
    private UserReader userReader;
    private UserStore userStore;
    private ImageStore imageStore;
    private PostStore postStore;
    private GymStore gymStore;
    private CommentStore commentStore;
    private ReviewStore reviewStore;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userReader = mock(UserReader.class);
        userStore = mock(UserStore.class);
        imageStore = mock(ImageStore.class);
        postStore = mock(PostStore.class);
        gymStore = mock(GymStore.class);
        commentStore = mock(CommentStore.class);
        reviewStore = mock(ReviewStore.class);

        userService = new UserServiceImpl(userReader, userStore, imageStore, postStore, commentStore, reviewStore, gymStore);
    }

    @Test
    @DisplayName("getUserDtoById(Long id) Test : userId 받아 UserDto 반환 [성공]")
    void when_UserId_Then_ReturnUserDtoSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);
        Optional<User> user = createwrappingOptional(createUser(1L));
        given(userReader.findUserById(anyLong())).willReturn(user);

        //when
        UserDto actualUserDto = userService.getUserDtoById(anyLong());

        //then
        then(userReader).should(times(1)).findUserById(anyLong());

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
                () -> userService.getUserDtoById(notExistsUserId));

        assertThat(exception.getStatus()).isEqualTo(UserErrorCode.NOT_FOUND.getStatus());
    }

    @Test
    @DisplayName("updateProfileImage(UserDto userDto, MultipartFile image) Test : 유저의 프로필 이미지를 업데이트한다. [성공]")
    void when_UpdateUserProfileImage_Then_UpdateSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);
        MultipartFile image = mock(MultipartFile.class);

        // when
        userService.updateProfileImage(userDto, image);

        // then
        then(imageStore).should(times(1)).delete(any(S3Folder.class), anyString());
        then(imageStore).should(times(1)).upload(any(S3Folder.class), any(MultipartFile.class));
        then(userStore).should(times(1)).save(any());
    }

    @Test
    @DisplayName("updateUserInfo(UserDto userDto) Test : UserDto로 유저정보를 업데이트한다. [성공]")
    void when_UpdateUserInfo_Then_UpdateSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);

        // when
        userService.updateUserInfo(userDto);

        // then
        then(userStore).should(times(1)).save(any());
    }

    @Test
    @DisplayName("deleteUser(UserDto userDto) Test : UserDto로 유저를 삭제한다. [성공]")
    void when_deleteUser_Then_DeleteSuccessfully() {
        //given
        UserDto userDto = createUserDto(1L);

        // when
        userService.deleteUser(userDto);

        // then
        then(gymStore).should(times(1)).deleteGymLikesByUserId(any());
        then(commentStore).should(times(1)).deleteCommentLikesByUserId(any());
        then(postStore).should(times(1)).deletePostLikesByUserId(any());
        then(postStore).should(times(1)).deletePostsByUserId(any());
        then(imageStore).should(times(1)).delete(any(S3Folder.class), anyString());
        then(userStore).should(times(1)).save(any());
    }

    private UserDto createUserDto(Long id) {
        return UserDto.of(
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
                UserStatus.ENABLE
        );
    }

    private User createUser(Long id) {
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
                UserStatus.ENABLE
        );
    }

    private UserDto createDeletedUserDto(Long id) {
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
                UserStatus.ENABLE
        );
    }

    public Optional<User> createwrappingOptional(User user) {
        return Optional.ofNullable(user);
    }

    public Optional<User> createEmptyOptional() {
        return Optional.empty();
    }


}