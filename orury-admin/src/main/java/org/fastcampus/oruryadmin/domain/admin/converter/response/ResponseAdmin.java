package org.fastcampus.oruryadmin.domain.admin.converter.response;

public record ResponseAdmin(
        String name,
        String email,
        String password,
        String accessToken
) {
    public static ResponseAdmin of(
            String name,
            String email,
            String password,
            String accessToken
    ) {
        return new ResponseAdmin(
                name,
                email,
                password,
                accessToken
        );
    }
}
