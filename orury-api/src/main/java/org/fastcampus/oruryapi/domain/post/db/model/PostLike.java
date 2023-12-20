package org.fastcampus.oruryapi.domain.post.db.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.base.db.AuditingField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"postLikePK"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "post_like")
public class PostLike extends AuditingField {
    @EmbeddedId
    private PostLikePK postLikePK;

    private PostLike(PostLikePK postLikePK) {
        this.postLikePK = postLikePK;
    }

    public static PostLike of(PostLikePK postLikePK) {
        return new PostLike(postLikePK);
    }
}
