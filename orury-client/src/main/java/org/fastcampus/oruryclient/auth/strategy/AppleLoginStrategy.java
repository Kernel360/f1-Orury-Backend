package org.fastcampus.oruryclient.auth.strategy;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.fastcampus.oruryclient.auth.converter.message.AuthMessage;
import org.fastcampus.oruryclient.auth.converter.request.LoginRequest;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.oruryclient.auth.strategy.applefeign.AppleAuthClient;
import org.fastcampus.orurycommon.error.code.AuthErrorCode;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.fastcampus.orurycommon.util.TokenDecoder;
import org.fastcampus.orurydomain.auth.dto.JwtToken;
import org.fastcampus.orurydomain.auth.dto.LoginDto;
import org.fastcampus.orurydomain.auth.dto.apple.AppleIdTokenPayload;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleLoginStrategy implements LoginStrategy {
    private static final int APPLE_SIGN_UP_TYPE = 2;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
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
    public LoginDto login(LoginRequest request) {
        String code = request.code();
        int signUpType = request.signUpType();

        String email = getEmailFromAuthorizationCode(code);

        // 애플 이메일이 없는 고객인 경우
        if (Objects.isNull(email)) {
            throw new AuthException(AuthErrorCode.NO_EMAIL);
        }
        Optional<User> user = userRepository.findByEmail(email);

        // 비회원인 경우
        if (user.isEmpty()) {
            return LoginDto.fromNoUser(signUpType, jwtTokenProvider.issueNoUserJwtTokens(email), AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage());
        }

        User userEntity = user.get();

        // 다른 소셜 로그인으로 가입한 회원인 경우
        if (userEntity.getSignUpType() != APPLE_SIGN_UP_TYPE) {
            throw new AuthException(AuthErrorCode.NOT_MATCHING_SOCIAL_PROVIDER);
        }

        // 정상 회원은 토큰 발급
        JwtToken jwtToken = jwtTokenProvider.issueJwtTokens(userEntity.getId(), userEntity.getEmail());
        return LoginDto.of(UserDto.from(userEntity), jwtToken, AuthMessage.LOGIN_SUCCESS.getMessage());
    }

    @Override
    public int getSignUpType() {
        return APPLE_SIGN_UP_TYPE;
    }

    private String getEmailFromAuthorizationCode(String authorizationCode) {

        String idToken = appleAuthClient.getIdToken(
                clientId,
                generateClientSecret(),
                grantType,
                authorizationCode
        ).idToken();

        return tokenDecoder.decodePayload(idToken, AppleIdTokenPayload.class).email();
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
//                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
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
