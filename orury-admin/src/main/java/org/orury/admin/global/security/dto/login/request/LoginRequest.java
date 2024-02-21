package org.orury.admin.global.security.dto.login.request;

public record LoginRequest(
        String email,
        String password
) {
}
