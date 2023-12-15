package org.fastcampus.oruryapi.domain.user.converter.request;

import org.fastcampus.oruryapi.domain.user.db.model.User;


public record RequestProfileImage (
        Long id,
        String profileImage
){
    public static RequestProfileImage of(

            Long id,
            String profileImage
    ){
        return new RequestProfileImage(id, profileImage);
    }

    public static RequestProfileImage from(User entity){
        return RequestProfileImage.of(
                entity.getId(),
                entity.getProfileImage());
    }
}
