package org.fastcampus.orurydomain.auth.dto.apple;

public record AppleIdTokenPayload(
        String sub,
        String email
) {
}
