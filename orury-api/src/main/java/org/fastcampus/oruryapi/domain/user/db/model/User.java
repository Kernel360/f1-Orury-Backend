package org.fastcampus.oruryapi.domain.user.db.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private int signupType;

    private int gender;

    private LocalDate birthday;

    private String profileImage;

    private User(String email, String nickname, int signupType, int gender, LocalDate birthday, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.signupType = signupType;
        this.gender = gender;
        this.birthday = birthday;
        this.profileImage = profileImage;
    }

    public static User of(String email, String nickname, int signupType, int gender, LocalDate birthday, String profileImage) {
        return new User(email, nickname, signupType, gender, birthday, profileImage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
