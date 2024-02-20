package org.oruryclient.user.db.model;

import org.orurydomain.global.constants.NumberConstants;
import org.orurydomain.user.db.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


class UserTest {

    @DisplayName("유저 생성 테스트")
    @Test
    void createUser() {
        //given
        User user = User.of(null, "abc@gmail.com", "testnick", "1234", 1, 2, LocalDate.of(2023, 12, 21), "orury.png", null, null, NumberConstants.IS_NOT_DELETED);

        //when


        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("abc@gmail.com");
        assertThat(user.getNickname()).isEqualTo("testnick");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getSignUpType()).isEqualTo(1);
        assertThat(user.getGender()).isEqualTo(2);
        assertThat(user.getBirthday()).isEqualTo("2023-12-21");
        assertThat(user.getProfileImage()).isEqualTo("orury.png");
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();

    }

}
