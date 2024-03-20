package org.orury.client.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.auth.application.oauth.AppleOAuthService;
import org.orury.client.auth.application.oauth.KakaoOAuthService;
import org.orury.client.auth.application.oauth.OAuthService;
import org.orury.client.auth.interfaces.message.AuthMessage;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.client.config.ServiceTest;
import org.orury.common.error.code.AuthErrorCode;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.orury.client.ClientFixtureFactory.TestLoginRequest.createLoginRequest;
import static org.orury.domain.DomainFixtureFactory.TestUser.createUser;
import static org.orury.domain.DomainFixtureFactory.TestUserDto.createUserDto;

@DisplayName("[Service] AuthServiceImpl 테스트")
class AuthServiceImplTest extends ServiceTest {

    @Test
    @DisplayName("email 중복되지 않는 경우, 회원정보 저장하고 JwtToken 발급하여 반환한다.")
    void when_NotDuplicatedEmail_Then_SaveUserAndRetrieveJwtToken() {
        // given
        String email = "orury@orury.com";
        UserDto userDto = createUserDto()
                .email(email).build().get();
        Long userId = 1L;
        User user = createUser()
                .id(userId)
                .email(email).build().get();

        given(userReader.findByEmail(email))
                .willReturn(Optional.of(user));

        // when
        authService.signUp(userDto);

        // then
        then(userStore).should(only())
                .saveAndFlush(any());
        then(userReader).should(times(1))
                .findByEmail(anyString());
        then(jwtTokenService).should(only())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("email 중복인 경우, 회원정보 저장 실패하고 DuplicatedUser 예외를 반환한다.")
    void when_DuplicatedEmail_Then_DuplicatedUserException() {
        // given
        String email = "orury@orury.com";
        UserDto userDto = createUserDto()
                .email(email).build().get();

        given(userStore.saveAndFlush(any()))
                .willThrow(DataIntegrityViolationException.class);

        // when & then
        Exception exception = assertThrows(BusinessException.class,
                () -> authService.signUp(userDto));

        assertEquals(UserErrorCode.DUPLICATED_USER.getMessage(), exception.getMessage());

        then(userStore).should(only())
                .saveAndFlush(any());
        then(userReader).should(never())
                .findByEmail(anyString());
        then(jwtTokenService).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("save된 유저의 email을 조회했으나, 존재하지 않는 User로 조회되면, NotExistingUserAccount 예외를 반환한다.")
    void when_UserWithSignUpEmailDoesNotExist_Then_NotExistingUserAccountException() {
        // given
        String email = "orury@orury.com";
        UserDto userDto = createUserDto()
                .email(email).build().get();

        given(userReader.findByEmail(email))
                .willReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(AuthException.class,
                () -> authService.signUp(userDto));

        assertEquals(AuthErrorCode.NOT_EXISTING_USER_ACCOUNT.getMessage(), exception.getMessage());

        then(userStore).should(times(1))
                .saveAndFlush(any());
        then(userReader).should(times(1))
                .findByEmail(anyString());
        then(jwtTokenService).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("정상 회원의 경우, 토큰을 발급하여 반환한다.")
    void when_NormalUser_Then_IssueAndRetrieveJwtToken() {
        // given
        int signUpType = 1;
        LoginRequest request = createLoginRequest()
                .signUpType(signUpType).build().get();
        OAuthService oAuthService = mock(KakaoOAuthService.class);
        String email = "Orury@kakao.com";
        User user = createUser()
                .email(email)
                .signUpType(signUpType).build().get();

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
        LoginRequest request = createLoginRequest()
                .signUpType(signUpType).build().get();
        OAuthService oAuthService = mock(AppleOAuthService.class);

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
        LoginRequest request = createLoginRequest()
                .signUpType(signUpType).build().get();
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
        LoginRequest request = createLoginRequest()
                .signUpType(signUpType).build().get();
        OAuthService oAuthService = mock(AppleOAuthService.class);
        String email = "Orury@apple.com";
        User user = createUser()
                .email(email)
                .signUpType(signUpType).build().get();

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
    @DisplayName("BAN(제재)된 회원의 경우, BanUser 예외를 반환한다.")
    void when_BannedUser_Then_BanUserException() {
        // given
        int signUpType = 1;
        LoginRequest request = createLoginRequest()
                .signUpType(signUpType).build().get();
        OAuthService oAuthService = mock(KakaoOAuthService.class);
        String email = "Orury@kakao.com";
        User banUser = createUser()
                .email(email)
                .signUpType(signUpType)
                .status(UserStatus.BAN).build().get();

        given(oAuthServiceManager.getOAuthService(signUpType))
                .willReturn(oAuthService);
        given(oAuthService.getEmailFromOAuthCode(request.code()))
                .willReturn(email);
        given(userReader.findByEmail(email))
                .willReturn(Optional.of(banUser));
        given(oAuthService.getSignUpType())
                .willReturn(signUpType);

        // when & then
        Exception exception = assertThrows(AuthException.class,
                () -> authService.login(request));

        assertEquals(AuthErrorCode.BAN_USER.getMessage(), exception.getMessage());

        then(oAuthServiceManager).should(only())
                .getOAuthService(anyInt());
        then(oAuthService).should(times(1))
                .getEmailFromOAuthCode(anyString());
        then(userReader).should(only())
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
}