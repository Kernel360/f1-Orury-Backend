package org.fastcampus.oruryclient.auth.strategy;

import org.fastcampus.oruryclient.auth.converter.message.AuthMessage;
import org.fastcampus.oruryclient.auth.converter.request.LoginRequest;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.oruryclient.auth.strategy.kakaofeign.KakaoAuthClient;
import org.fastcampus.oruryclient.auth.strategy.kakaofeign.KakaoKapiClient;
import org.fastcampus.orurycommon.error.code.AuthErrorCode;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.fastcampus.orurydomain.auth.dto.JwtToken;
import org.fastcampus.orurydomain.auth.dto.LoginDto;
import org.fastcampus.orurydomain.auth.dto.kakao.KakaoAccount;
import org.fastcampus.orurydomain.auth.dto.kakao.KakaoAccountDto;
import org.fastcampus.orurydomain.auth.dto.kakao.KakaoOAuthTokenDto;
import org.fastcampus.orurydomain.auth.dto.kakao.Profile;
import org.fastcampus.orurydomain.global.constants.NumberConstants;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Kakao OAuth 인증  테스트")
@ActiveProfiles("test")
class KakaoLoginStrategyTest {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private KakaoAuthClient kakaoAuthClient;
    private KakaoKapiClient kakaoKapiClient;
    private KakaoLoginStrategy kakaoLoginStrategy;

    private static final int KAKAO_SIGN_UP_TYPE = 1;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        kakaoAuthClient = mock(KakaoAuthClient.class);
        kakaoKapiClient = mock(KakaoKapiClient.class);
        kakaoLoginStrategy = new KakaoLoginStrategy(userRepository, jwtTokenProvider, kakaoAuthClient, kakaoKapiClient);
    }

    @Test
    void getKakaoSignUpType() {
        assertEquals(
                KAKAO_SIGN_UP_TYPE,
                kakaoLoginStrategy.getSignUpType()
        );
    }

    @Test
    @DisplayName("Kakao로 가입한 회원일 경우, 로그인 성공")
    void when_UserWithKakao_Then_LoginSuccessfully() {
        // given
        LoginRequest loginRequest = new LoginRequest("code", KAKAO_SIGN_UP_TYPE);
        KakaoOAuthTokenDto kakaoOAuthTokenDto = createKakaoOAuthTokenDto();
        KakaoAccountDto kakaoAccountDto = createKakaoAccountDto();
        User kakaoUser = createUser(KAKAO_SIGN_UP_TYPE);
        JwtToken jwtToken = new JwtToken("acc", "ref");

        given(kakaoAuthClient.getIdToken(any(), any(), any(), anyString(), any()))
                .willReturn(kakaoOAuthTokenDto);
        given(kakaoKapiClient.getInfo(anyString(), any()))
                .willReturn(kakaoAccountDto);
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(kakaoUser));
        given(jwtTokenProvider.issueJwtTokens(anyLong(), anyString()))
                .willReturn(jwtToken);

        // when
        LoginDto loginDto = kakaoLoginStrategy.login(loginRequest);

        //then
        assertEquals(
                AuthMessage.LOGIN_SUCCESS.getMessage(),
                loginDto.flag()
        );

        then(userRepository).should(times(1))
                .findByEmail(anyString());
        then(jwtTokenProvider).should(never())
                .issueNoUserJwtTokens(anyString());
        then(jwtTokenProvider).should(times(1))
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("비회원일 경우, 비회원용 Jwt토큰 발급")
    void when_NoUser_Then_IssueNoUserJwtTokens() {
        // given
        LoginRequest loginRequest = new LoginRequest("code", KAKAO_SIGN_UP_TYPE);
        KakaoOAuthTokenDto kakaoOAuthTokenDto = createKakaoOAuthTokenDto();
        KakaoAccountDto kakaoAccountDto = createKakaoAccountDto();
        JwtToken noUserjwtToken = new JwtToken("acc", "ref");

        given(kakaoAuthClient.getIdToken(any(), any(), any(), anyString(), any()))
                .willReturn(kakaoOAuthTokenDto);
        given(kakaoKapiClient.getInfo(anyString(), any()))
                .willReturn(kakaoAccountDto);
        given(jwtTokenProvider.issueNoUserJwtTokens(anyString()))
                .willReturn(noUserjwtToken);

        // when
        LoginDto loginDto = kakaoLoginStrategy.login(loginRequest);

        //then
        assertEquals(
                AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage(),
                loginDto.flag()
        );

        then(userRepository).should(times(1))
                .findByEmail(anyString());
        then(jwtTokenProvider).should(times(1))
                .issueNoUserJwtTokens(anyString());
        then(jwtTokenProvider).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("다른 소셜로 가입한 회원일 경우, NotMatchingSocialProvider Exception 반환")
    void when_UserWithDifferentSocial_Then_NotMatchingSocialProviderException() {
        // given
        int appleSignUpType = 2;
        LoginRequest loginRequest = new LoginRequest("code", KAKAO_SIGN_UP_TYPE);
        KakaoOAuthTokenDto kakaoOAuthTokenDto = createKakaoOAuthTokenDto();
        KakaoAccountDto kakaoAccountDto = createKakaoAccountDto();
        User kakaoUser = createUser(appleSignUpType);
        JwtToken jwtToken = new JwtToken("acc", "ref");

        given(kakaoAuthClient.getIdToken(any(), any(), any(), anyString(), any()))
                .willReturn(kakaoOAuthTokenDto);
        given(kakaoKapiClient.getInfo(anyString(), any()))
                .willReturn(kakaoAccountDto);
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(kakaoUser));

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> kakaoLoginStrategy.login(loginRequest));

        assertEquals(
                AuthErrorCode.NOT_MATCHING_SOCIAL_PROVIDER.getStatus(),
                exception.getStatus()
        );

        then(userRepository).should(times(1))
                .findByEmail(anyString());
        then(jwtTokenProvider).should(never())
                .issueNoUserJwtTokens(anyString());
        then(jwtTokenProvider).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    @Test
    @DisplayName("이메일 값이 넘어오지 않으면, NoEmail Exception 반환")
    void when_NoEmail_Then_NoEmailException() {
        // given
        LoginRequest loginRequest = new LoginRequest("code", KAKAO_SIGN_UP_TYPE);
        KakaoOAuthTokenDto kakaoOAuthTokenDto = createKakaoOAuthTokenDto();
        KakaoAccountDto kakaoAccountDtoWithNoEmail = createKakaoAccountDtoWithNoEmail();

        given(kakaoAuthClient.getIdToken(any(), any(), any(), anyString(), any()))
                .willReturn(kakaoOAuthTokenDto);
        given(kakaoKapiClient.getInfo(anyString(), any()))
                .willReturn(kakaoAccountDtoWithNoEmail);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> kakaoLoginStrategy.login(loginRequest));

        assertEquals(
                AuthErrorCode.NO_EMAIL.getStatus(),
                exception.getStatus()
        );

        then(userRepository).should(never())
                .findByEmail(anyString());
        then(jwtTokenProvider).should(never())
                .issueNoUserJwtTokens(anyString());
        then(jwtTokenProvider).should(never())
                .issueJwtTokens(anyLong(), anyString());
    }

    private static KakaoOAuthTokenDto createKakaoOAuthTokenDto() {
        return new KakaoOAuthTokenDto(
                "testAccessToken",
                "testTokenType",
                "testRefreshToken",
                "testIdToken",
                100,
                200,
                "email"
        );
    }

    private static KakaoAccountDto createKakaoAccountDto() {
        return new KakaoAccountDto(
                1L,
                true,
                LocalDateTime.of(2024, 2, 8, 22, 00),
                LocalDateTime.of(2024, 2, 8, 22, 00),
                new KakaoAccount(new Profile("testNickname"), "test@orury.com")
        );
    }

    private static User createUser(int signUpType) {
        return User.of(
                1L,
                "userEmail",
                "userNickname",
                "userPassword",
                signUpType,
                1,
                LocalDate.now(),
                "userProfileImage",
                LocalDateTime.of(1999, 3, 1, 7, 50),
                LocalDateTime.of(1999, 3, 1, 7, 50),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private static KakaoAccountDto createKakaoAccountDtoWithNoEmail() {
        return new KakaoAccountDto(
                1L,
                true,
                LocalDateTime.of(2024, 2, 8, 22, 00),
                LocalDateTime.of(2024, 2, 8, 22, 00),
                new KakaoAccount(new Profile("testNickname"), null)
        );
    }
}
