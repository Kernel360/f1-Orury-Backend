package org.orury.domain.auth.domain.dto.apple;

public record AppleIdTokenPayload(
        String sub,
        String email
) {
}
