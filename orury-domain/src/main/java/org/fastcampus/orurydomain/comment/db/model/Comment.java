package org.fastcampus.orurydomain.comment.db.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurydomain.base.db.AuditingField;
import org.fastcampus.orurydomain.post.db.model.Post;
import org.fastcampus.orurydomain.user.db.model.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "comment")
public class Comment extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "deleted", nullable = false)
    private int deleted;

    private Comment(Long id, String content, Long parentId, Post post, User user, int deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.post = post;
        this.user = user;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Comment of(Long id, String content, Long parentId, Post post, User user, int deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Comment(id, content, parentId, post, user, deleted, createdAt, updatedAt);
    }
}
