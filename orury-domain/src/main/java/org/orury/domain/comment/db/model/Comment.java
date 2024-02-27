package org.orury.domain.comment.db.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.base.db.AuditingField;
import org.orury.domain.global.constants.Constants;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.user.db.model.User;
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

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "deleted", nullable = false)
    private int deleted;

    private Comment(Long id, String content, Long parentId, int likeCount, Post post, User user, int deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.likeCount = likeCount;
        this.post = post;
        this.user = user;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Comment of(Long id, String content, Long parentId, int likeCount, Post post, User user, int deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Comment(id, content, parentId, likeCount, post, user, deleted, createdAt, updatedAt);
    }

    public Comment delete() {
        this.content = Constants.DELETED_COMMENT_CONTENT.getMessage();
        this.deleted = NumberConstants.IS_DELETED;
        this.likeCount = 0;
        return this;
    }
}
