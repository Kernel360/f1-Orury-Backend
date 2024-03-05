package org.orury.client.auth.application.oauth;

public interface OAuthService {
    String getEmailFromOAuthCode(String oAuthCode);

    int getSignUpType();
}
