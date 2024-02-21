package org.orury.domain.gym.db.model;

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
public class GymLikePK implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "gym_id", nullable = false)
    private Long gymId;

    private GymLikePK(Long userId, Long gymId) {
        this.userId = userId;
        this.gymId = gymId;
    }

    public static GymLikePK of(Long userId, Long gymId) {
        return new GymLikePK(
                userId,
                gymId
        );
    }
}