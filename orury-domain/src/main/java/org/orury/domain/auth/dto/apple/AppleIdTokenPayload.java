package org.orury.domain.auth.dto.apple;

public record AppleIdTokenPayload(
        String sub,
        String email
) {
}
