package org.orury.client.auth.application.oauth;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.orury.client.auth.application.oauth.applefeign.AppleAuthClient;
import org.orury.common.util.TokenDecoder;
import org.orury.domain.auth.domain.dto.apple.AppleIdTokenPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleOAuthService implements OAuthService {
    private static final int APPLE_SIGN_UP_TYPE = 2;
    private final TokenDecoder tokenDecoder;
    private final AppleAuthClient appleAuthClient;

    @Value("${oauth-login.provider.apple.grant-type}")
    String grantType;
    @Value("${oauth-login.provider.apple.client-id}")
    String clientId;
    @Value("${oauth-login.provider.apple.key-id}")
    String keyId;
    @Value("${oauth-login.provider.apple.team-id}")
    String teamId;
    @Value("${oauth-login.provider.apple.audience}")
    String audience;
    @Value("${oauth-login.provider.apple.private-key}")
    String privateKey;

    @Override
    public String getEmailFromOAuthCode(String code) {
        String idToken = appleAuthClient.getIdToken(
                clientId,
                generateClientSecret(),
                grantType,
                code
        ).idToken();
        return tokenDecoder.decodePayload(idToken, AppleIdTokenPayload.class).email();
    }

    @Override
    public int getSignUpType() {
        return APPLE_SIGN_UP_TYPE;
    }

    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
        return Jwts.builder()
                .header().keyId(keyId).and()
                .issuer(teamId)
                .audience().add(audience).and()
                .subject(clientId)
                .expiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .issuedAt(new Date())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }
}
