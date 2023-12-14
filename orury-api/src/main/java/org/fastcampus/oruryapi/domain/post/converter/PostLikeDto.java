package org.fastcampus.oruryapi.domain.post.converter;

import org.fastcampus.oruryapi.domain.post.db.model.PostLike;
import org.fastcampus.oruryapi.domain.user.db.model.User;

public record PostLikeDto(
        User user,
        Post post
) {
    public static PostLikeDto of(
            User user,
            Post post
    ){
        return new PostLikeDto(
                user,
                post
        );
    }

    public static PostLikeDto from(PostLike entity){
        return PostLikeDto.of(
                entity.getUser(),
                entity.getPost()
        );
    }

    public PostLike toEntity(){
        return PostLike.of(
                this
        );
    }



}
