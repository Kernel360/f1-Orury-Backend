package org.orurydomain.auth.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.orurydomain.base.db.AuditingField;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"userId"}, callSuper = false)
@Entity(name = "refresh_token")
public class RefreshToken extends AuditingField {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "value", nullable = false)
    private String value;

    public RefreshToken(
            Long userId,
            String value,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.userId = userId;
        this.value = value;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RefreshToken of(
            Long userId,
            String value,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new RefreshToken(
                userId,
                value,
                createdAt,
                updatedAt
        );
    }
}

