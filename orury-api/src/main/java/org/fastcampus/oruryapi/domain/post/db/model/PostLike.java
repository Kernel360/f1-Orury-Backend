package org.fastcampus.oruryapi.domain.post.db.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "post_like")
public class PostLike {
    @EmbeddedId
    private PostLikePK postLikePK;

    private PostLike(PostLikePK postLikePK) {
        this.postLikePK = postLikePK;
    }

    public static PostLike of(PostLikePK postLikePK) {
        return new PostLike(postLikePK);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLike postLike)) return false;
        return Objects.equals(postLikePK, postLike.postLikePK);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(postLikePK);
    }
}
