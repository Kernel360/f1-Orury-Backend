package org.fastcampus.oruryapi.base.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ToString
@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingField {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    protected LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = true, name = "updated_at")
    protected LocalDateTime updatedAt;
}