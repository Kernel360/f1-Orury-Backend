package org.orury.client.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.auth.application.jwt.JwtTokenService;
import org.orury.client.auth.application.jwt.JwtTokenServiceImpl;
import org.orury.common.error.code.TokenErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.domain.auth.domain.RefreshTokenReader;
import org.orury.domain.auth.domain.RefreshTokenStore;
import org.orury.domain.auth.domain.dto.JwtToken;
import org.orury.domain.global.constants.Constants;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] JwtTokenServiceImpl 테스트")
@ActiveProfiles("test")
class JwtTokenServiceImplTest {

    private JwtTokenService jwtTokenService;
    private String secret;
    private RefreshTokenReader refreshTokenReader;
    private RefreshTokenStore refreshTokenStore;
    private UserReader userReader;

    // Jwt토큰 유저 정보
    private final Long TOKEN_USER_ID = 1L;
    private final String TOKEN_USER_EMAIL = "email@orury.com";

    // 유효한 토큰
    private final String VALID_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNDUyLCJleHAiOjIzMTEwNDM0NTJ9.LtE-2BG5o-EO-SelasvEdyKNXMWNJaSagbBhcpHjxp0";
    private final String VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNDUyLCJleHAiOjI5MTU4NDM0NTJ9.AUXxm1cVPW3eOw2QCUTgU5QciyoL9w3oHyHkRhiQF8M";
    private final String VALID_NO_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJlbWFpbCI6ImVtYWlsQG9ydXJ5LmNvbSIsImlhdCI6MTcwOTUyMDcwMCwiZXhwIjo0ODE5OTIwNzAwfQ.FyLQ5vmZs2qayBhQ-WW8r_IimOYhFvr-5A8qfUJxRag";

    // 만료된 토큰
    private final String EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNzE2LCJleHAiOjE3MDYyNDM3NzZ9.nTKrQ7HxWiS9dG0WixQ6F578ugkSfN2TosXUnHr_fpc";
    private final String EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNzE2LCJleHAiOjE3MDYyNDM4MzZ9.v8AKma4QS3zCg1UzejUPf-uuY5BpwM7OiACNljY-QxM";
    private final String EXPIRED_NO_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJlbWFpbCI6ImVtYWlsQG9ydXJ5LmNvbSIsImlhdCI6MTcwODYwMTgzOCwiZXhwIjoxNzA4NjAzNjM4fQ.nljAg1le8v6I0H6EKcF1pcgLzRUxY_jHHiQKKygA6mg";

    @BeforeEach
    public void setUp() {
        secret = "=============================================JwtTokenSecretForOruryTestCode=============================================";
        refreshTokenReader = mock(RefreshTokenReader.class);
        refreshTokenStore = mock(RefreshTokenStore.class);
        userReader = mock(UserReader.class);

        jwtTokenService = new JwtTokenServiceImpl(secret, refreshTokenReader, refreshTokenStore, userReader);
    }

    @DisplayName("만료되지 않고 유효한 형식의 액세스토큰이 들어오면, 액세스토큰으로부터 생성한 인증객체를 정상적으로 반환한다.")
    @Test
    void should_RetrieveAuthenticationFromNormalAccessToken() {
        // given
        String accessTokenHeader = "Bearer " + VALID_ACCESS_TOKEN;
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        User user = createUser();
        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);
        given(userReader.findUserById(1L)).willReturn(Optional.of(user));

        Long userId = TOKEN_USER_ID;
        String userEmail = TOKEN_USER_EMAIL;

        UserPrincipal expectedPrincipal = UserPrincipal.fromToken(
                userId,
                userEmail,
                Constants.ROLE_USER.getMessage()
        );
        String expectedCredentials = "";
        List<SimpleGrantedAuthority> expectedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(Constants.ROLE_USER.getMessage()));

        // when
        Authentication authentication = jwtTokenService.getAuthenticationFromRequest(mockRequest);

        // then
        assertEquals(
                new UsernamePasswordAuthenticationToken(expectedPrincipal, expectedCredentials, expectedAuthorities),
                authentication
        );
    }

    @DisplayName("만료되지 않고 유효한 형식의 비회원토큰이 들어오면, 비회원토큰으로부터 생성한 인증객체를 정상적으로 반환한다.")
    @Test
    void should_RetrieveAuthenticationFromNormalNoUserToken() {
        // given
        String accessTokenHeader = "Bearer " + VALID_NO_USER_TOKEN;
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        String userEmail = TOKEN_USER_EMAIL;

        UserPrincipal expectedPrincipal = UserPrincipal.fromToken(
                0L,
                userEmail,
                Constants.ROLE_USER.getMessage()
        );
        String expectedCredentials = "";
        List<SimpleGrantedAuthority> expectedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(Constants.ROLE_USER.getMessage()));

        // when
        Authentication authentication = jwtTokenService.getAuthenticationFromRequest(mockRequest);

        // then
        assertEquals(
                new UsernamePasswordAuthenticationToken(expectedPrincipal, expectedCredentials, expectedAuthorities),
                authentication
        );
    }

    @DisplayName("Authorization 헤더에 담긴 토큰이 없으면, InvalidAccessToken 예외를 발생시킨다.")
    @Test
    void when_NullValueInAuthorizationHeader_Then_InvalidAccessTokenException() {
        // given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        given(mockRequest.getHeader("Authorization"))
                .willReturn(null);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.getAuthenticationFromRequest(mockRequest));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("Authorization 헤더에 담긴 토큰 prefix가 \"Bearer \"가 아니면, InvalidAccessToken 예외를 발생시킨다.")
    @Test
    void when_invalidPrefixAccessToken_Then_InvalidAccessTokenException() {
        // given
        String accessTokenHeader = "Orury " + VALID_ACCESS_TOKEN;
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.getAuthenticationFromRequest(mockRequest));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("유효하지 않은 형식의 액세스토큰이 들어오면, InvalidAccessToken 예외를 반환한다.")
    @Test
    void when_MalFormedAccessToken_Then_InvalidAccessTokenException() {
        // given
        String accessTokenHeader = "Bearer " + VALID_ACCESS_TOKEN.substring(0, 20);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.getAuthenticationFromRequest(mockRequest));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("Jwt토큰이 아닌 값이 들어오면, InvalidAccessToken 예외를 반환한다.")
    @Test
    void when_IllegalArgument_Then_InvalidAccessTokenException() {
        // given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        String accessTokenHeader = "Bearer " + "IamNOTjwtTOKEN";
        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.getAuthenticationFromRequest(mockRequest));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("만료된 액세스토큰이 들어오면, ExpiredAccessToken 예외를 반환한다.")
    @Test
    void when_ExpiredAccessToken_Then_ExpiredAccessTokenException() {
        // given
        String accessTokenHeader = "Bearer " + EXPIRED_ACCESS_TOKEN;
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.getAuthenticationFromRequest(mockRequest));

        assertEquals(TokenErrorCode.EXPIRED_ACCESS_TOKEN.getStatus(), exception.getStatus());
        assertEquals(TokenErrorCode.EXPIRED_ACCESS_TOKEN.getMessage(), exception.getMessage());
    }

    @DisplayName("만료된 비회원용 액세스토큰이 들어오면, ExpiredNoUserToken 예외를 반환한다.")
    @Test
    void when_ExpiredNoUserAccessToken_Then_ExpiredAccessTokenException() {
        // given
        String accessTokenHeader = "Bearer " + EXPIRED_NO_USER_TOKEN;
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.getAuthenticationFromRequest(mockRequest));

        assertEquals(TokenErrorCode.EXPIRED_NO_USER_TOKEN.getStatus(), exception.getStatus());
        assertEquals(TokenErrorCode.EXPIRED_NO_USER_TOKEN.getMessage(), exception.getMessage());
    }

    @DisplayName("만료되지 않고 유효한 형식의 리프레쉬토큰으로부터 생성한 인증객체를 정상적으로 반환한다.")
    @Test
    void should_RetrieveAuthenticationFromNormalRefreshToken() {
        // given
        String refreshTokenHeader = "Bearer " + VALID_REFRESH_TOKEN;
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);
        given(refreshTokenReader.existsByValue(anyString()))
                .willReturn(true);

        // when
        jwtTokenService.reissueJwtTokens(request);

        // then
        then(refreshTokenReader).should(times(1))
                .existsByValue(anyString());
    }

    @DisplayName("리프레쉬토큰이 null로 들어오면, InvalidRefreshToken 예외를 반환한다.")
    @Test
    void when_NullRefreshToken_Then_InvalidRefreshTokenException() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("Authorization")).willReturn(null);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());

        then(refreshTokenReader).should(never())
                .existsByValue(anyString());
    }

    @DisplayName("리프레쉬토큰 prefix가 \"Bearer \"가 아니면, InvalidRefreshToken 예외를 반환한다.")
    @Test
    void when_invalidPrefixRefreshToken_Then_InvalidRefreshTokenException() {
        // given
        String refreshTokenHeader = "Orury " + VALID_REFRESH_TOKEN;
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());

        then(refreshTokenReader).should(never())
                .existsByValue(anyString());
    }

    @DisplayName("유효하지 않은 형식의 리프레쉬토큰이 들어오면, InvalidRefreshToken 예외를 반환한다.")
    @Test
    void when_MalFormedRefreshToken_Then_InvalidRefreshTokenException() {
        // given
        String refreshTokenHeader = "Bearer " + VALID_REFRESH_TOKEN.substring(0, 20);
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());

        then(refreshTokenReader).should(never())
                .existsByValue(anyString());
    }

    @DisplayName("Jwt토큰이 아닌 값이 들어오면, InvalidRefreshToken 예외를 반환한다.")
    @Test
    void when_IllegalArgument_Then_InvalidRefreshTokenException() {
        // given
        String refreshTokenHeader = "Bearer " + "IamNOTjwtTOKEN";
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());

        then(refreshTokenReader).should(never())
                .existsByValue(anyString());
    }

    @DisplayName("만료된 리프레쉬토큰이 들어오면, ExpiredRefreshToken 예외를 반환한다.")
    @Test
    void when_ExpiredRefreshToken_Then_ExpiredRefreshTokenException() {
        // given
        String refreshTokenHeader = "Bearer " + EXPIRED_REFRESH_TOKEN;
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.EXPIRED_REFRESH_TOKEN.getStatus(), exception.getStatus());

        then(refreshTokenReader).should(never())
                .existsByValue(anyString());
    }

    @DisplayName("갱신되기 전의 리프레시토큰이 들어오면, ExpiredRefreshToken 예외를 반환한다.")
    @Test
    void when_RefreshTokenBeforeReissue_Then_ExpiredRefreshTokenException() {
        // given
        String refreshTokenHeader = "Bearer " + VALID_REFRESH_TOKEN;
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);

        given(refreshTokenReader.existsByValue(anyString()))
                .willReturn(false);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenService.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.EXPIRED_REFRESH_TOKEN.getStatus(), exception.getStatus());

        then(refreshTokenReader).should(times(1))
                .existsByValue(anyString());
    }

    @DisplayName("id와 email을 받으면, 액세스토큰과 리프레시토큰을 생성하고 저장하여 돌려준다.")
    @Test
    void when_IdAndEmail_Then_CreateAndSaveJwtTokens() {
        // given
        Long userId = 2L;
        String userEmail = "orury2@orury.com";

        // when
        jwtTokenService.issueJwtTokens(userId, userEmail);

        // then
        then(refreshTokenStore).should(times(1))
                .save(anyLong(), any());
    }

    @DisplayName("email을 받으면, 액세스토큰만을 생성하여 반환한다.")
    @Test
    void when_Email_Then_RetrieveNoUserToken() {
        // given
        String email = "orury1@orury.com";

        // when
        JwtToken jwtToken = jwtTokenService.issueNoUserJwtTokens(email);

        // then
        assertNotNull(jwtToken.accessToken());
        assertNull(jwtToken.refreshToken());

    }

    private User createUser() {
        return User.of(
                TOKEN_USER_ID,
                TOKEN_USER_EMAIL,
                "nick",
                "pw",
                1,
                1,
                null,
                null,
                null,
                null,
                UserStatus.ENABLE
        );
    }
}
