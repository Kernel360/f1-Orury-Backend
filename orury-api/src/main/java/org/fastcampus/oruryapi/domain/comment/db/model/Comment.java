package org.fastcampus.oruryapi.domain.comment.db.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.db.AuditingField;
import org.fastcampus.oruryapi.domain.post.db.model.Post;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Long parentId;

    @ManyToOne(optional = false)
    private Post post;

    @ManyToOne(optional = false)
    private User user;

    private Comment(String content, Long parentId, Post post, User user) {
        this.content = content;
        this.parentId = parentId;
        this.post = post;
        this.user = user;
    }

    public static Comment of(String content, Long parentId, Post post, User user) {
        return new Comment(content, parentId, post, user);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
