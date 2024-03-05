package org.orury.client.auth.application.oauth;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OAuthServiceManager {

    private final Map<Integer, OAuthService> loginServiceMap;

    public OAuthServiceManager(List<OAuthService> strategies) {
        loginServiceMap = strategies.stream()
                .collect(Collectors.toMap(OAuthService::getSignUpType,
                        Function.identity()));
    }

    public OAuthService getOAuthService(int signUpType) {
        return loginServiceMap.get(signUpType);
    }
}