package org.orury.client.auth.application.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.auth.application.oauth.kakaofeign.KakaoAuthClient;
import org.orury.client.auth.application.oauth.kakaofeign.KakaoKapiClient;
import org.orury.domain.auth.domain.dto.kakao.KakaoOAuthTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuthService implements OAuthService {
    private static final int KAKAO_SIGN_UP_TYPE = 1;

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoKapiClient kakaoKapiClient;

    @Value("${oauth-login.provider.kakao.grant-type}")
    String grantType;
    @Value("${oauth-login.provider.kakao.client-id}")
    String clientId;
    @Value("${oauth-login.provider.kakao.redirect-uri}")
    String redirectURI;
    @Value("${oauth-login.provider.kakao.client-secret}")
    String clientSecret;
    @Value("${oauth-login.provider.kakao.content-type}")
    String contentType;

    @Override
    public String getEmailFromOAuthCode(String code) {
        KakaoOAuthTokenDto kakaoOAuthToken = kakaoAuthClient.getIdToken(
                grantType,
                clientId,
                redirectURI,
                code,
                clientSecret
        );
        return getEmailFromToken(kakaoOAuthToken.accessToken());
    }

    @Override
    public int getSignUpType() {
        return KAKAO_SIGN_UP_TYPE;
    }

    private String getEmailFromToken(String kakaoAccessToken) {
        return kakaoKapiClient.getInfo(
                "Bearer " + kakaoAccessToken,
                contentType
        ).kakaoAccount().email();
    }
}
