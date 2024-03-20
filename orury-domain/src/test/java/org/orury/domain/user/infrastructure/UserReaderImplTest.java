package org.orury.domain.user.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.user.domain.entity.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.orury.domain.UserDomainFixture.TestUser.createUser;

@DisplayName("[UserReaderImpl] User ReaderImpl 테스트")
class UserReaderImplTest extends InfrastructureTest {

    @Test
    @DisplayName("findUserById(Long id) Test : User id가 들어오면 Optional<User>를 반환한다. [성공]")
    void should_ReturnOptional() {
        //given
        Long userId = 1L;
        Optional<User> user = Optional.of(createUser(userId).build().get());
        given(userRepository.findById(anyLong())).willReturn(user);

        //when
        Optional<User> actualUser = userReader.findUserById(userId);

        //then
        assertThat(actualUser).isEqualTo(user);
    }
}