package org.fastcampus.oruryadmin.global.security.dto.login.request;

public record LoginRequest(
        String email,
        String password
) {
}
