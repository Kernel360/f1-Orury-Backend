package org.orury.client.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.orury.client.auth.application.jwt.JwtTokenService;
import org.orury.client.auth.application.oauth.OAuthService;
import org.orury.client.auth.application.oauth.OAuthServiceManager;
import org.orury.client.auth.interfaces.message.AuthMessage;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.common.error.code.AuthErrorCode;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.auth.domain.dto.JwtToken;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.auth.domain.dto.SignUpDto;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserReader userReader;
    private final UserStore userStore;
    private final JwtTokenService jwtTokenService;
    private final OAuthServiceManager oAuthServiceManager;

    @Transactional
    @Override
    public SignUpDto signUp(UserDto userDto) {
        try {
            userStore.saveAndFlush(userDto.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(UserErrorCode.DUPLICATED_USER);
        }
        User user = userReader.findByEmail(userDto.email())
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_EXISTING_USER_ACCOUNT));
        JwtToken jwtToken = jwtTokenService.issueJwtTokens(user.getId(), userDto.email());
        return SignUpDto.of(userDto, jwtToken);
    }

    @Transactional
    @Override
    public LoginDto login(LoginRequest request) {
        OAuthService oAuthService = oAuthServiceManager.getOAuthService(request.signUpType());
        String email = oAuthService.getEmailFromOAuthCode(request.code());

        // 이메일이 없는 고객인 경우
        if (Objects.isNull(email)) {
            throw new AuthException(AuthErrorCode.NO_EMAIL);
        }

        User user = userReader.findByEmail(email).orElseGet(() -> null);

        // 비회원인 경우
        if (Objects.isNull(user)) {
            return LoginDto.fromNoUser(oAuthService.getSignUpType(), jwtTokenService.issueNoUserJwtTokens(email), AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage());
        }

        // 다른 소셜 로그인으로 가입한 회원인 경우
        if (user.getSignUpType() != oAuthService.getSignUpType()) {
            throw new AuthException(AuthErrorCode.NOT_MATCHING_SOCIAL_PROVIDER);
        }

        // 관리자에 의해 제재된 유저
        if (user.getStatus() == UserStatus.BAN)
            throw new AuthException(AuthErrorCode.BAN_USER);

        // 정상 회원은 토큰 발급
        JwtToken jwtToken = jwtTokenService.issueJwtTokens(user.getId(), user.getEmail());
        return LoginDto.of(UserDto.from(user), jwtToken, AuthMessage.LOGIN_SUCCESS.getMessage());
    }

    @Transactional
    @Override
    public JwtToken reissueJwtTokens(HttpServletRequest request) {
        return jwtTokenService.reissueJwtTokens(request);
    }
}
