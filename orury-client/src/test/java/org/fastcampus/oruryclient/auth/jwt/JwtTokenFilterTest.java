package org.fastcampus.oruryclient.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.fastcampus.orurycommon.error.code.TokenErrorCode;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenFilter 테스트")
@ActiveProfiles("test")
class JwtTokenFilterTest {

    private JwtTokenFilter jwtTokenFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);

        jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
    }

    @DisplayName("JwtTokenFilter의 대상이고 jwtToken Exception이 발생하지 않으면, 정상적으로 doFilterInternal()을 수행한다.")
    @Test
    void should_DoFilterInternalSuccessfully() throws ServletException, IOException {
        // given
        request.setRequestURI("/api/v1/posts/1");

        // when
        jwtTokenFilter.doFilter(request, response, filterChain);

        // then
        then(filterChain).should()
                .doFilter(any(), any());

        then(jwtTokenProvider).should()
                .getTokenFromRequest(any());
        then(jwtTokenProvider).should()
                .getAuthenticationFromAccessToken(any());
    }

    @DisplayName("getTokenFromRequest에서 InvalidException이 발생하면, 필터를 거치지 않는다.")
    @Test
    void when_InvalidExceptionByGetTokenFromRequest_Then_DoNotPassFilter() throws ServletException, IOException {
        // given
        request.setRequestURI("/api/v1/comments/2");

        given(jwtTokenProvider.getTokenFromRequest(request))
                .willThrow(new AuthException(TokenErrorCode.INVALID_ACCESS_TOKEN));

        // when
        jwtTokenFilter.doFilter(request, response, filterChain);

        // then
        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), response.getStatus());

        then(filterChain).should(never())
                .doFilter(request, response);

        then(jwtTokenProvider).should()
                .getTokenFromRequest(any());
        then(jwtTokenProvider).should(never())
                .getAuthenticationFromAccessToken(any());
    }

    @DisplayName("getAuthenticationFromAccessToken에서 InvalidException이 발생하면, 필터를 거치지 않는다.")
    @Test
    void when_InvalidExceptionByGetAuthenticationFromAccessToken_Then_DoNotPassFilter() throws ServletException, IOException {
        // given
        request.setRequestURI("/api/v1/reviews/3");

        given(jwtTokenProvider.getAuthenticationFromAccessToken(any()))
                .willThrow(new AuthException(TokenErrorCode.INVALID_ACCESS_TOKEN));

        // when
        jwtTokenFilter.doFilter(request, response, filterChain);

        // then
        assertEquals(TokenErrorCode.INVALID_ACCESS_TOKEN.getStatus(), response.getStatus());

        then(filterChain).should(never())
                .doFilter(request, response);

        then(jwtTokenProvider).should()
                .getTokenFromRequest(any());
        then(jwtTokenProvider).should()
                .getAuthenticationFromAccessToken(any());
    }

    @DisplayName("getAuthenticationFromAccessToken에서 ExpiredException이 발생하면, 필터를 거치지 않는다.")
    @Test
    void when_ExpiredExceptionByGetAuthenticationFromAccessToken_Then_DoNotPassFilter() throws ServletException, IOException {
        // given
        request.setRequestURI("/api/v1/gyms/4");

        given(jwtTokenProvider.getAuthenticationFromAccessToken(any()))
                .willThrow(new AuthException(TokenErrorCode.EXPIRED_ACCESS_TOKEN));

        // when
        jwtTokenFilter.doFilter(request, response, filterChain);

        // then
        assertEquals(TokenErrorCode.EXPIRED_ACCESS_TOKEN.getStatus(), response.getStatus());

        then(filterChain).should(never())
                .doFilter(request, response);

        then(jwtTokenProvider).should()
                .getTokenFromRequest(any());
        then(jwtTokenProvider).should()
                .getAuthenticationFromAccessToken(any());
    }

    @DisplayName("JwtTokenFilter의 대상 request가 아니라면, 정상적으로 shouldNotFilter()을 수행한다.")
    @Test
    void when_ExcludePath_Then_should_ShouldNotFilterSuccessfully() throws ServletException, IOException {
        // given
        String[] excludePath = {"/api/v1/auth", "/swagger-ui", "/v3/api-docs", "/favicon.ico"};

        for (String path : excludePath) {
            request.setRequestURI(path + "/temp-path");

            // when
            jwtTokenFilter.doFilter(request, response, filterChain);
        }

        // then
        then(filterChain).should(times(excludePath.length))
                .doFilter(any(), any());

        then(jwtTokenProvider).should(never())
                .getTokenFromRequest(any());
        then(jwtTokenProvider).should(never())
                .getAuthenticationFromAccessToken(any());
    }
}
