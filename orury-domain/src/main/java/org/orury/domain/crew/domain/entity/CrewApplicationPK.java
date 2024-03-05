package org.orury.domain.crew.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class CrewApplicationPK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "crew_id", nullable = false)
    private Long crewId;

    private CrewApplicationPK(Long userId, Long crewId) {
        this.userId = userId;
        this.crewId = crewId;
    }

    public static CrewApplicationPK of(Long userId, Long crewId) {
        return new CrewApplicationPK(userId, crewId);
    }
}
