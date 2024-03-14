package org.orury.domain.notification.domain.entity;

import org.orury.domain.base.db.AuditingField;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity(name = "notification")
public class Notification extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "is_read", nullable = false)
    private int isRead;

    private Notification(
            Long id,
            User user,
            String content,
            String url,
            int isRead,
            LocalDateTime createdAt,
            LocalDateTime updateAt
    ) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.url = url;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.updatedAt = updateAt;
    }

    public static Notification of(
            Long id,
            User user,
            String content,
            String url,
            int isRead,
            LocalDateTime createdAt,
            LocalDateTime updateAt
    ) {
        return new Notification(
                id,
                user,
                content,
                url,
                isRead,
                createdAt,
                updateAt
        );
    }
}
