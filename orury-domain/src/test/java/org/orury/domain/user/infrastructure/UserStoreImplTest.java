package org.orury.domain.user.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.user.domain.entity.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@DisplayName("[UserStoreImpl] User StoreImpl 테스트")
class UserStoreImplTest extends InfrastructureTest {

    @Test
    @DisplayName("save(Long id) Test: User Entity가 들어오면 해당하는 엔티티를 저장한다. [성공]")
    void should_saveUserEntity() {
        //given
        User user = mock(User.class);

        //when
        userStore.save(user);

        //then
        then(userRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("delete(Long id) Test: User Entity가 들어오면 해당하는 엔티티를 삭제한다. [성공]")
    void should_deleteUserEntity() {
        //given
        Long userId = 1L;

        //when
        userStore.delete(userId);

        //then
        then(userRepository).should(times(1))
                .deleteById(userId);
    }

    @Test
    @DisplayName("saveAndFlush(Long id) Test: User Entity가 들어오면 해당하는 엔티티를 저장하고 flush한다. [성공]")
    void should_saveAndFlushUserEntity() {
        //given
        User user = mock(User.class);

        //when
        userStore.saveAndFlush(user);

        //then
        then(userRepository).should(times(1))
                .saveAndFlush(any());
    }
}