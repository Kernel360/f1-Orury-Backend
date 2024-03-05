package org.orury.domain.crew.domain.entity;

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
@EqualsAndHashCode(of = {"crewMemberPK"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "crew_member")
public class CrewMember extends AuditingField {
    @EmbeddedId
    private CrewMemberPK crewMemberPK;

    private CrewMember(
            CrewMemberPK crewMemberPK,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.crewMemberPK = crewMemberPK;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CrewMember of(
            CrewMemberPK crewMemberPK,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CrewMember(
                crewMemberPK,
                createdAt,
                updatedAt
        );
    }
}
