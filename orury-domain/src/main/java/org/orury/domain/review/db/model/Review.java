package org.orury.domain.review.db.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.base.db.AuditingField;
import org.orury.domain.global.listener.EntityImageConverter;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.user.db.model.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ToString
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "review")
public class Review extends AuditingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content")
    private String content;

    @Convert(converter = EntityImageConverter.class)
    @Column(name = "images")
    private List<String> images;

    @Column(name = "score", nullable = false)
    private float score;

    @Column(name = "interest_count", nullable = false)
    private int interestCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "help_count", nullable = false)
    private int helpCount;

    @Column(name = "thumb_count", nullable = false)
    private int thumbCount;

    @Column(name = "angry_count", nullable = false)
    private int angryCount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    public Review(
            Long id,
            String content,
            List<String> images,
            float score,
            int interestCount,
            int likeCount,
            int helpCount,
            int thumbCount,
            int angryCount,
            User user,
            Gym gym,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.content = content;
        this.images = images;
        this.score = score;
        this.interestCount = interestCount;
        this.likeCount = likeCount;
        this.helpCount = helpCount;
        this.thumbCount = thumbCount;
        this.angryCount = angryCount;
        this.user = user;
        this.gym = gym;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Review of(
            Long id,
            String content,
            List<String> images,
            float score,
            int interestCount,
            int likeCount,
            int helpCount,
            int thumbCount,
            int angryCount,
            User user,
            Gym gym,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Review(
                id,
                content,
                images,
                score,
                interestCount,
                likeCount,
                helpCount,
                thumbCount,
                angryCount,
                user,
                gym,
                createdAt,
                updatedAt
        );
    }
}
