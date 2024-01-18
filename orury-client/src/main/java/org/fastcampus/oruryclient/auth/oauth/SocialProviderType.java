package org.fastcampus.oruryclient.auth.oauth;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SocialProviderType {
    KAKAO(1, "kakao"),
    APPLE(2, "apple");

    private final int signUpType;
    private final String providerName;

    SocialProviderType(int signUpType, String providerName) {
        this.signUpType = signUpType;
        this.providerName = providerName;
    }

    public static int convertToIntType(String providerName) {
        return Arrays.stream(SocialProviderType.values())
                .filter(e -> e.getProviderName().equals(providerName))
                .findAny()
                .map(SocialProviderType::getSignUpType)
                .orElseThrow(() -> null);
    }
}