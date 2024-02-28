package org.orury.domain.user.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.entity.User;
import org.orury.domain.user.infrastucture.UserReaderImpl;
import org.orury.domain.user.infrastucture.UserRepository;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("[UserReaderImpl] User ReaderImpl 테스트")
@ActiveProfiles("test")
class UserReaderImplTest {
    private UserRepository userRepository;
    private UserReaderImpl userReaderImpl;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userReaderImpl = new UserReaderImpl(userRepository);
    }

    @Test
    @DisplayName("findUserById(Long id) Test : User id가 들어오면 Optional<User>를 반환한다. [성공]")
    void should_ReturnOptional() {
        //given
        Long userId = 1L;
        Optional<User> user = createwrappingOptional(createUser(1L));
        given(userRepository.findById(anyLong())).willReturn(user);

        //when
        Optional<User> actualUser = userReaderImpl.findUserById(userId);

        //then
        assertThat(actualUser).isEqualTo(user);
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

    public static Optional<User> createwrappingOptional(User user) {
        return Optional.ofNullable(user);
    }

    public static Optional<User> createEmptyOptional() {
        return Optional.empty();
    }


}