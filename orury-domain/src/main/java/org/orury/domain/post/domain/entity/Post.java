package org.orury.domain.post.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.base.db.AuditingField;
import org.orury.domain.global.listener.EntityImageConverter;
import org.orury.domain.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity(name = "post")
public class Post extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Convert(converter = EntityImageConverter.class)
    @Column(name = "images")
    private List<String> images;

    @Column(name = "category", nullable = false)
    private int category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Post(
            Long id,
            String title,
            String content,
            int viewCount,
            int commentCount,
            int likeCount,
            List<String> images,
            int category,
            User user,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.images = images;
        this.category = category;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Post of(Long id, String title, String content, int viewCount, int commentCount, int likeCount, List<String> images, int category, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Post(id, title, content, viewCount, commentCount, likeCount, images, category, user, createdAt, updatedAt);
    }
}
