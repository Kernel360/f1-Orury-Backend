package org.orury.domain.gym.db.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"gymLikePK"})
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "gym_like")
public class GymLike {
    @EmbeddedId
    private GymLikePK gymLikePK;

    private GymLike(GymLikePK gymLikePK) {
        this.gymLikePK = gymLikePK;
    }

    public static GymLike of(GymLikePK gymLikePK) {
        return new GymLike(gymLikePK);
    }
}
