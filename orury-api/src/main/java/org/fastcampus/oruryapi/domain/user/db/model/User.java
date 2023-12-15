package org.fastcampus.oruryapi.domain.user.db.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.db.AuditingField;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestProfileImage;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestUserInfo;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User extends AuditingField{
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

    private User(String email, String nickname, String password, int signupType, int gender, LocalDate birthday, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.signupType = signupType;
        this.gender = gender;
        this.birthday = birthday;
        this.profileImage = profileImage;
    }

    public void updateProfileImage(RequestProfileImage requestProfileImage){
        this.profileImage = requestProfileImage.profileImage();
    }

    public void updateUserInfo(RequestUserInfo requestUserInfo){
        this.nickname = requestUserInfo.nickname();
    }


    public static User of(String email, String nickname, String password, int signupType, int gender, LocalDate birthday, String profileImage) {
        return new User(email, nickname, password, signupType, gender, birthday, profileImage);
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