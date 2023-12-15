package org.fastcampus.oruryapi.domain.user.converter.request;

import org.fastcampus.oruryapi.domain.user.db.model.User;

public record RequestUserInfo(
        Long id,
        String nickname
) {
    public static RequestUserInfo of(
            Long id,
            String nickname
    ){
        return new RequestUserInfo(id, nickname);
    }

    public static RequestUserInfo from(User entity){
        return RequestUserInfo.of(
                entity.getId(),
                entity.getNickname()
        );
    }
}
