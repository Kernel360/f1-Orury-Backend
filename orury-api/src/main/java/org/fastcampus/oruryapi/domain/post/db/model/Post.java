package org.fastcampus.oruryapi.domain.post.db.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.db.AuditingField;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "images")
    private String images;

    @Column(name = "category", nullable = false)
    private int category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Post(String title, String content, String images, int category, User user) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.category = category;
        this.user = user;
    }

    public static Post of(String title, String content, String images, int category, User user) {
        return new Post(title, content, images, category, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
