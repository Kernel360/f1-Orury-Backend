package org.orury.client.auth.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.auth.converter.message.AuthMessage;
import org.orury.client.auth.converter.request.LoginRequest;
import org.orury.client.auth.jwt.JwtTokenProvider;
import org.orury.client.auth.strategy.kakaofeign.KakaoAuthClient;
import org.orury.client.auth.strategy.kakaofeign.KakaoKapiClient;
import org.orury.common.error.code.AuthErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.domain.auth.dto.JwtToken;
import org.orury.domain.auth.dto.LoginDto;
import org.orury.domain.auth.dto.kakao.KakaoOAuthTokenDto;
import org.orury.domain.user.db.model.User;
import org.orury.domain.user.db.repository.UserRepository;
import org.orury.domain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginStrategy implements LoginStrategy {
    private static final int KAKAO_SIGN_UP_TYPE = 1;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

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
    public LoginDto login(LoginRequest request) {
        String code = request.code();
        int signUpType = request.signUpType();

        String kakaoToken = getKakaoTokenFromAuthorizationCode(code).accessToken();
        String email = getEmailFromToken(kakaoToken);

        // 카카오 이메일이 없는 고객인 경우
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
        if (userEntity.getSignUpType() != KAKAO_SIGN_UP_TYPE) {
            throw new AuthException(AuthErrorCode.NOT_MATCHING_SOCIAL_PROVIDER);
        }

        // 정상 회원은 토큰 발급
        JwtToken jwtToken = jwtTokenProvider.issueJwtTokens(userEntity.getId(), userEntity.getEmail());
        return LoginDto.of(UserDto.from(userEntity), jwtToken, AuthMessage.LOGIN_SUCCESS.getMessage());
    }

    @Override
    public int getSignUpType() {
        return KAKAO_SIGN_UP_TYPE;
    }

    private KakaoOAuthTokenDto getKakaoTokenFromAuthorizationCode(String code) {
        return kakaoAuthClient.getIdToken(
                grantType,
                clientId,
                redirectURI,
                code,
                clientSecret
        );
    }

    private String getEmailFromToken(String kakaoAccessToken) {
        return kakaoKapiClient.getInfo(
                "Bearer " + kakaoAccessToken,
                contentType
        ).kakaoAccount().email();
    }
}
