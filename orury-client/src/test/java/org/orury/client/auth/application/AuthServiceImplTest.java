package org.orury.client.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.auth.application.jwt.JwtTokenService;
import org.orury.client.auth.application.oauth.AppleOAuthService;
import org.orury.client.auth.application.oauth.KakaoOAuthService;
import org.orury.client.auth.application.oauth.OAuthService;
import org.orury.client.auth.application.oauth.OAuthServiceManager;
import org.orury.client.auth.interfaces.message.AuthMessage;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.common.error.code.AuthErrorCode;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.entity.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] AuthServiceImpl 테스트")
@ActiveProfiles("test")
class AuthServiceImplTest {
    private AuthService authService;
    private UserReader userReader;
    private UserStore userStore;
    private JwtTokenService jwtTokenService;
    private OAuthServiceManager oAuthServiceManager;

    @BeforeEach
    void setUp() {
        userReader = mock(UserReader.class);
        userStore = mock(UserStore.class);
        jwtTokenService = mock(JwtTokenService.class);
        oAuthServiceManager = mock(OAuthServiceManager.class);

        authService = new AuthServiceImpl(userReader, userStore, jwtTokenService, oAuthServiceManager);
    }

    @Test
    @DisplayName("email 중복되지 않는 경우, 회원정보 저장하고 JwtToken 발급하여 반환한다.")
    void when_NotDuplicatedEmail_Then_SaveUserAndRetrieveJwtToken() {
        // given
        UserDto userDto = createUserDto();

        // when
        authService.signUp(userDto);

        // then
        then(userStore).should(times(1))
                .saveAndFlush(any());
        then(jwtTokenService).should(times(1))
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("email 중복인 경우, 회원정보 저장 실패하고 DuplicatedUser 예외를 반환한다.")
    void when_DuplicatedEmail_Then_DuplicatedUserException() {
        // given
        UserDto userDto = createUserDto();

        given(userStore.saveAndFlush(any()))
                .willThrow(DataIntegrityViolationException.class);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> authService.signUp(userDto));

        assertEquals(UserErrorCode.DUPLICATED_USER.getMessage(), exception.getMessage());

        then(userStore).should(times(1))
                .saveAndFlush(any());
        then(jwtTokenService).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("정상 회원의 경우, 토큰을 발급하여 반환한다.")
    void when_NormalUser_Then_IssueAndRetrieveJwtToken() {
        // given
        int signUpType = 1;
        LoginRequest request = createLoginRequest(signUpType);
        OAuthService oAuthService = mock(KakaoOAuthService.class);
        String email = "Orury@kakao.com";
        User user = createUser(email, signUpType);

        given(oAuthServiceManager.getOAuthService(signUpType))
                .willReturn(oAuthService);
        given(oAuthService.getEmailFromOAuthCode(request.code()))
                .willReturn(email);
        given(userReader.findByEmail(email))
                .willReturn(Optional.of(user));
        given(oAuthService.getSignUpType())
                .willReturn(signUpType);

        // when
        LoginDto loginDto = authService.login(request);

        // then
        assertEquals(AuthMessage.LOGIN_SUCCESS.getMessage(), loginDto.flag());

        then(oAuthServiceManager).should(times(1))
                .getOAuthService(anyInt());
        then(oAuthService).should(times(1))
                .getEmailFromOAuthCode(anyString());
        then(userReader).should(times(1))
                .findByEmail(anyString());
        then(oAuthService).should(times(1))
                .getSignUpType();
        then(jwtTokenService).should(times(1))
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("OAuth인증된 이메일이 null인 경우, NoEmail 예외를 반환한다.")
    void when_AuthenticatedEmailIsNull_Then_NoEmailException() {
        // given
        int signUpType = 2;
        LoginRequest request = createLoginRequest(signUpType);
        OAuthService oAuthService = mock(AppleOAuthService.class);
        String email = "Orury@apple.com";

        given(oAuthServiceManager.getOAuthService(signUpType))
                .willReturn(oAuthService);
        given(oAuthService.getEmailFromOAuthCode(request.code()))
                .willReturn(null);

        // when & then
        Exception exception = assertThrows(AuthException.class,
                () -> authService.login(request));

        assertEquals(AuthErrorCode.NO_EMAIL.getMessage(), exception.getMessage());

        then(oAuthServiceManager).should(times(1))
                .getOAuthService(anyInt());
        then(oAuthService).should(times(1))
                .getEmailFromOAuthCode(anyString());
        then(userReader).should(never())
                .findByEmail(anyString());
        then(oAuthService).should(never())
                .getSignUpType();
        then(jwtTokenService).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("비회원인 경우, 비회원토큰을 발급하고 LoginDto의 flag에 비회원메세지를 담아 반환한다.")
    void when_NoUser_CreateNoUserTokenAndRetrieveFlagWithNoUserMessage() {
        // given
        int signUpType = 1;
        LoginRequest request = createLoginRequest(signUpType);
        OAuthService oAuthService = mock(KakaoOAuthService.class);
        String email = "Orury@kakao.com";

        given(oAuthServiceManager.getOAuthService(signUpType))
                .willReturn(oAuthService);
        given(oAuthService.getEmailFromOAuthCode(request.code()))
                .willReturn(email);
        given(userReader.findByEmail(email))
                .willReturn(Optional.empty());

        // when
        LoginDto loginDto = authService.login(request);

        // then
        assertEquals(AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage(), loginDto.flag());

        then(oAuthServiceManager).should(times(1))
                .getOAuthService(anyInt());
        then(oAuthService).should(times(1))
                .getEmailFromOAuthCode(anyString());
        then(userReader).should(times(1))
                .findByEmail(anyString());
        then(oAuthService).should(times(1))
                .getSignUpType();
        then(jwtTokenService).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("인증한 OAuth가 아닌 다른 OAuth로 가입한 회원의 경우, NotMatchingSocialProvider 예외를 반환한다.")
    void when_NotMatchingAuthenticatedOAuthAndSignUpType_Then_NotMatchingSocialProviderException() {
        // given
        int signUpType = 2;
        int anotherSignUpType = 1;
        LoginRequest request = createLoginRequest(signUpType);
        OAuthService oAuthService = mock(AppleOAuthService.class);
        String email = "Orury@apple.com";
        User user = createUser(email, signUpType);

        given(oAuthServiceManager.getOAuthService(signUpType))
                .willReturn(oAuthService);
        given(oAuthService.getEmailFromOAuthCode(request.code()))
                .willReturn(email);
        given(userReader.findByEmail(email))
                .willReturn(Optional.of(user));
        given(oAuthService.getSignUpType())
                .willReturn(anotherSignUpType);

        // when & then
        Exception exception = assertThrows(AuthException.class,
                () -> authService.login(request));

        assertEquals(AuthErrorCode.NOT_MATCHING_SOCIAL_PROVIDER.getMessage(), exception.getMessage());

        then(oAuthServiceManager).should(times(1))
                .getOAuthService(anyInt());
        then(oAuthService).should(times(1))
                .getEmailFromOAuthCode(anyString());
        then(userReader).should(times(1))
                .findByEmail(anyString());
        then(oAuthService).should(times(1))
                .getSignUpType();
        then(jwtTokenService).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("HttpServletRequest를 받아 JwtToken을 재발급하여 반환한다.")
    void when_HttpServletRequest_Then_ReissueAndRetrieveJwtToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when
        authService.reissueJwtTokens(request);

        // then
        then(jwtTokenService).should(times(1))
                .reissueJwtTokens(any());
    }

    private static UserDto createUserDto() {
        return UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.png",
                LocalDateTime.now(),
                LocalDateTime.now(),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private LoginRequest createLoginRequest(int signUpType) {
        return LoginRequest.of(
                "OAuth_Authentication_Code",
                signUpType
        );
    }

    private User createUser(String email, int signUpType) {
        return User.of(
                1L,
                email,
                "userNickname",
                "userPassword",
                signUpType,
                1,
                null,
                "userProfileImage",
                null,
                null,
                NumberConstants.IS_NOT_DELETED
        );
    }
}