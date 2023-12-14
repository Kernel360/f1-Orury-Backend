package org.fastcampus.oruryapi.domain.user.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.db.AuditingField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Slf4j
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private int signup_type;

    private int gender;

    private LocalDate birthday;

    private int type;

    private String profile_image;

    //private User(String email, String nickname, String password, int signup_type, )

}
