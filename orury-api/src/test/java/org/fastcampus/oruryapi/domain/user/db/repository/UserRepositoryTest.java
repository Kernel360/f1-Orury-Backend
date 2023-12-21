package org.fastcampus.oruryapi.domain.user.db.repository;

import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @DisplayName("생성된 유저를 저장할 때 id, createdAt, updatedAt을 자동으로 생성해주는지 테스트")
    @Test
    @Transactional
    void saveAutoTest() {
        //given
        User user = User.of(null,"abc@gmail.com","testnick", "1234", 1,2, LocalDate.of(2023, 12, 21),"orury.png",null, null);

        //when
        User saveUser = userRepository.save(user);

        //then
        assertThat(saveUser.getId()).isInstanceOf(Long.class);
        assertThat(saveUser.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(saveUser.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }
}