package org.orury.domain.crew.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.NumericBooleanConverter;
import org.orury.domain.base.db.AuditingField;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewGenderConverter;
import org.orury.domain.global.domain.Region;
import org.orury.domain.global.domain.RegionConverter;
import org.orury.domain.global.listener.CrewImageConverter;
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

    @Convert(converter = RegionConverter.class)
    @Column(name = "region", nullable = false)
    private Region region;

    @Column(name = "description", nullable = true)
    private String description;

    @Convert(converter = CrewImageConverter.class)
    @Column(name = "icon", nullable = true)
    private String icon;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(name = "max_age", nullable = false)
    private int maxAge;

    @Convert(converter = CrewGenderConverter.class)
    @Column(name = "gender", nullable = false)
    private CrewGender gender;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "permission_required", nullable = false)
    private boolean permissionRequired;

    @Column(name = "question", nullable = false)
    private String question;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "answer_required", nullable = false)
    private boolean answerRequired;

    private Crew(
            Long id,
            String name,
            int memberCount,
            int capacity,
            Region region,
            String description,
            String icon,
            boolean isDeleted,
            User user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int minAge,
            int maxAge,
            CrewGender gender,
            boolean permissionRequired,
            String question,
            boolean answerRequired
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
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.permissionRequired = permissionRequired;
        this.question = question;
        this.answerRequired = answerRequired;
    }

    public static Crew of(
            Long id,
            String name,
            int memberCount,
            int capacity,
            Region region,
            String description,
            String icon,
            boolean isDeleted,
            User user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int minAge,
            int maxAge,
            CrewGender gender,
            boolean permissionRequired,
            String question,
            boolean answerRequired
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
                updatedAt,
                minAge,
                maxAge,
                gender,
                permissionRequired,
                question,
                answerRequired
        );
    }
}
