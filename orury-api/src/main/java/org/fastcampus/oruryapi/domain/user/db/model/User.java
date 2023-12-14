package org.fastcampus.oruryapi.domain.user.db.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.db.AuditingField;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class User extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private int signupType;

    private int gender;

    private LocalDate birthday;

    private String profileImage;

    public User(Long id, String email, String nickname, String password, int signupType, int gender, LocalDate birthday, String profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.signupType = signupType;
        this.gender = gender;
        this.birthday = birthday;
        this.profileImage = profileImage;
    }

    public static User of(UserDto userDto){
        return new User(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                userDto.password(),
                userDto.signupType(),
                userDto.gender(),
                userDto.birthday(),
                userDto.profileImage()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
