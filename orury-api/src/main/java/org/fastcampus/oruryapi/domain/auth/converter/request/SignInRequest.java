package org.fastcampus.oruryapi.domain.auth.converter.request;

public record SignInRequest(
        int signUpType,
        String email
) {
}