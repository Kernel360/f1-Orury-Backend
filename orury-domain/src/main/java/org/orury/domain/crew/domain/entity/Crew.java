package org.orury.domain.crew.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.base.db.AuditingField;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity(name = "crew")
public class Crew extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "member_count", nullable = false)
    private int memberCount;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Column(name = "is_deleted", nullable = false)
    private int isDeleted;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Crew(
            Long id,
            String name,
            int memberCount,
            int capacity,
            String region,
            String description,
            String icon,
            int isDeleted,
            User user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.capacity = capacity;
        this.region = region;
        this.description = description;
        this.icon = icon;
        this.isDeleted = isDeleted;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Crew of(
            Long id,
            String name,
            int memberCount,
            int capacity,
            String region,
            String description,
            String icon,
            int isDeleted,
            User user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Crew(
                id,
                name,
                memberCount,
                capacity,
                region,
                description,
                icon,
                isDeleted,
                user,
                createdAt,
                updatedAt
        );
    }
}
