package org.fastcampus.oruryapi.domain.user.db.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.db.AuditingField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "user")
@EqualsAndHashCode(of={"id"}, callSuper = false)
public class User extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "sign_up_type", nullable = false)
    private int signUpType;

    @Column(name = "gender", nullable = false)
    private int gender;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "profile_image")
    private String profileImage;

    private User(String email, String nickname, String password, int signUpType, int gender, LocalDate birthday, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.signUpType = signUpType;
        this.gender = gender;
        this.birthday = birthday;
        this.profileImage = profileImage;
    }

    public static User of(String email, String nickname, String password, int signUpType, int gender, LocalDate birthday, String profileImage) {
        return new User(email, nickname, password, signUpType, gender, birthday, profileImage);
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
