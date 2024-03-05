package org.orury.domain.crew.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.base.db.AuditingField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"crewApplicationPK"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "crew_application")
public class CrewApplication extends AuditingField {
    @EmbeddedId
    private CrewApplicationPK crewApplicationPK;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "is_declined", nullable = false)
    private int isDeclined;

    private CrewApplication(
            CrewApplicationPK crewApplicationPK,
            String description,
            int isDeclined,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.crewApplicationPK = crewApplicationPK;
        this.description = description;
        this.isDeclined = isDeclined;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CrewApplication of(
            CrewApplicationPK crewApplicationPK,
            String description,
            int isDeclined,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CrewApplication(
                crewApplicationPK,
                description,
                isDeclined,
                createdAt,
                updatedAt
        );
    }
}
