package org.orury.domain.meeting.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.base.db.AuditingField;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity(name = "crew_meeting")
public class Meeting extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "member_count", nullable = false)
    private int memberCount;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @ManyToOne(optional = false)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    private Meeting(
            Long id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int memberCount,
            int capacity,
            User user,
            Gym gym,
            Crew crew,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memberCount = memberCount;
        this.capacity = capacity;
        this.user = user;
        this.gym = gym;
        this.crew = crew;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Meeting of(
            Long id,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int memberCount,
            int capacity,
            User user,
            Gym gym,
            Crew crew,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Meeting(
                id,
                startTime,
                endTime,
                memberCount,
                capacity,
                user,
                gym,
                crew,
                createdAt,
                updatedAt
        );
    }
}
