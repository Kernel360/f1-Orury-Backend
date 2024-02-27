package org.orury.client.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.common.error.code.TokenErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.domain.auth.db.repository.RefreshTokenRepository;
import org.orury.domain.global.constants.Constants;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenProvider 테스트")
@ActiveProfiles("test")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private String secret;
    private RefreshTokenRepository refreshTokenRepository;

    // Jwt토큰 유저 정보
    private final Long TOKEN_USER_ID = 1L;
    private final String TOKEN_USER_EMAIL = "email@orury.com";

    // 유효한 토큰
    private final String VALID_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNDUyLCJleHAiOjIzMTEwNDM0NTJ9.LtE-2BG5o-EO-SelasvEdyKNXMWNJaSagbBhcpHjxp0";
    private final String VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNDUyLCJleHAiOjI5MTU4NDM0NTJ9.AUXxm1cVPW3eOw2QCUTgU5QciyoL9w3oHyHkRhiQF8M";

    // 만료된 토큰
    private final String EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNzE2LCJleHAiOjE3MDYyNDM3NzZ9.nTKrQ7HxWiS9dG0WixQ6F578ugkSfN2TosXUnHr_fpc";
    private final String EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJpZCI6MSwiaWF0IjoxNzA2MjQzNzE2LCJleHAiOjE3MDYyNDM4MzZ9.v8AKma4QS3zCg1UzejUPf-uuY5BpwM7OiACNljY-QxM";
    private final String EXPIRED_NO_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBvcnVyeS5jb20iLCJlbWFpbCI6ImVtYWlsQG9ydXJ5LmNvbSIsImlhdCI6MTcwODYwMTgzOCwiZXhwIjoxNzA4NjAzNjM4fQ.nljAg1le8v6I0H6EKcF1pcgLzRUxY_jHHiQKKygA6mg";

    @BeforeEach
    public void setUp() {
        secret = "=============================================JwtTokenSecretForOruryTestCode=============================================";
        refreshTokenRepository = mock(RefreshTokenRepository.class);

        jwtTokenProvider = new JwtTokenProvider(secret, refreshTokenRepository);
    }

    @DisplayName("HttpServletRequest의 Authorization 헤더가 들어가있으면, Prefix(Bearer )를 제거한 jwt 토큰을 반환해야 한다.")
    @Test
    void should_ReturnAccessTokenTokenWithoutPrefix() {
        // given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        String accessTokenHeader = "Bearer " + VALID_ACCESS_TOKEN;
        String expectedToken = VALID_ACCESS_TOKEN;

        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        // when
        String actualToken = jwtTokenProvider.getTokenFromRequest(mockRequest);

        // then
        assertEquals(expectedToken, actualToken);
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
                () -> jwtTokenProvider.getTokenFromRequest(mockRequest));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("Authorization 헤더에 담긴 토큰 prefix가 \"Bearer \"가 아니면, InvalidAccessToken 예외를 발생시킨다.")
    @Test
    void when_invalidPrefixAccessToken_Then_InvalidAccessTokenException() {
        // given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        String accessTokenHeader = "Orury " + VALID_ACCESS_TOKEN;

        given(mockRequest.getHeader("Authorization"))
                .willReturn(accessTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenProvider.getTokenFromRequest(mockRequest));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("만료되지 않고 유효한 형식의 액세스토큰이 들어오면, 액세스토큰으로부터 생성한 인증객체를 정상적으로 반환한다.")
    @Test
    void should_RetrieveAuthenticationFromNormalAccessToken() {
        // given
        String accessToken = VALID_ACCESS_TOKEN;

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
        Authentication authentication = jwtTokenProvider.getAuthenticationFromAccessToken(accessToken);

        // then
        assertEquals(
                new UsernamePasswordAuthenticationToken(expectedPrincipal, expectedCredentials, expectedAuthorities),
                authentication
        );
    }

    @DisplayName("유효하지 않은 형식의 액세스토큰이 들어오면, InvalidAccessToken 예외를 반환한다.")
    @Test
    void when_MalFormedAccessToken_Then_InvalidAccessTokenException() {
        // given
        String accessToken = VALID_ACCESS_TOKEN.substring(0, 20);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenProvider.getAuthenticationFromAccessToken(accessToken));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("Jwt토큰이 아닌 값이 들어오면, InvalidAccessToken 예외를 반환한다.")
    @Test
    void when_IllegalArgument_Then_InvalidAccessTokenException() {
        // given
        String accessToken = "IamNOTjwtTOKEN";

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenProvider.getAuthenticationFromAccessToken(accessToken));

        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("만료된 액세스토큰이 들어오면, ExpiredAccessToken 예외를 반환한다.")
    @Test
    void when_ExpiredAccessToken_Then_ExpiredAccessTokenException() {
        // given
        String accessToken = EXPIRED_ACCESS_TOKEN;

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenProvider.getAuthenticationFromAccessToken(accessToken));

        assertEquals(TokenErrorCode.EXPIRED_ACCESS_TOKEN.getStatus(), exception.getStatus());
        assertEquals(TokenErrorCode.EXPIRED_ACCESS_TOKEN.getMessage(), exception.getMessage());
    }

    @DisplayName("만료된 비회원용 액세스토큰이 들어오면, ExpiredNoUserToken 예외를 반환한다.")
    @Test
    void when_ExpiredNoUserAccessToken_Then_ExpiredAccessTokenException() {
        // given
        String accessToken = EXPIRED_NO_USER_TOKEN;

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenProvider.getAuthenticationFromAccessToken(accessToken));

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
        given(refreshTokenRepository.existsByValue(anyString()))
                .willReturn(true);

        // when
        jwtTokenProvider.reissueJwtTokens(request);

        // then
        then(refreshTokenRepository).should()
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
                () -> jwtTokenProvider.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());
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
                () -> jwtTokenProvider.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());
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
                () -> jwtTokenProvider.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());
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
                () -> jwtTokenProvider.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.INVALID_REFRESH_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("만료된 리프레쉬토큰이 들어오면, ExpiredRefreshToken 예외를 반환한다.")
    @Test
    void when_ExpiredRefreshToken_Then_ExpiredRefreshTokenException() {
        // given
        String refreshTokenHeader = "Bearer " + VALID_REFRESH_TOKEN;
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenProvider.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.EXPIRED_REFRESH_TOKEN.getStatus(), exception.getStatus());
    }

    @DisplayName("갱신되기 전의 리프레시토큰이 들어오면, ExpiredRefreshToken 예외를 반환한다.")
    @Test
    void when_RefreshTokenBeforeReissue_Then_ExpiredRefreshTokenException() {
        // given
        String refreshTokenHeader = "Bearer " + VALID_REFRESH_TOKEN;
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn(refreshTokenHeader);

        given(refreshTokenRepository.existsByValue(anyString()))
                .willReturn(false);

        // when & then
        AuthException exception = assertThrows(AuthException.class,
                () -> jwtTokenProvider.reissueJwtTokens(request));

        assertEquals(TokenErrorCode.EXPIRED_REFRESH_TOKEN.getStatus(), exception.getStatus());

        then(refreshTokenRepository).should()
                .existsByValue(anyString());
    }

    @DisplayName("id와 email을 받으면, 액세스토큰과 리프레시토큰을 생성하고 저장하여 돌려준다.")
    @Test
    void when_IdAndEmail_Then_CreateAndSaveJwtTokens() {
        // given
        Long userId = 2L;
        String userEmail = "orury2@orury.com";

        // when
        jwtTokenProvider.issueJwtTokens(userId, userEmail);

        // then
        then(refreshTokenRepository).should()
                .save(any());
    }
}
