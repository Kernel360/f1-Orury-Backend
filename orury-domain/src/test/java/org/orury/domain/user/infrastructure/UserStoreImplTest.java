package org.orury.domain.user.infrastructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.entity.User;
import org.orury.domain.user.infrastucture.UserRepository;
import org.orury.domain.user.infrastucture.UserStoreImpl;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
@DisplayName("[UserStoreImpl] User StoreImpl 테스트")
@ActiveProfiles("test")
class UserStoreImplTest {
    private UserRepository userRepository;
    private UserStoreImpl userStoreImpl;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userStoreImpl = new UserStoreImpl(userRepository);
    }

    @Test
    @DisplayName("save(Long id) Test: User Entity가 들어오면 해당하는 엔티티를 저장한다. [성공]")
    void should_saveUserEntity() {
        //given
        User user = createUser(1L);

        //when
        userStoreImpl.save(user);

        //then
        then(userRepository).should(times(1)).save(any());
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
}