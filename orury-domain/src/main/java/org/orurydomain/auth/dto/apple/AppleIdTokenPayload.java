package org.orurydomain.auth.dto.apple;

public record AppleIdTokenPayload(
        String sub,
        String email
) {
}
