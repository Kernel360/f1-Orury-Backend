package org.fastcampus.oruryclient.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.fastcampus.orurycommon.error.code.TokenErrorCode;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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
    void temp1() throws ServletException, IOException {
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

    @DisplayName("request 헤더에 유효한 토큰이 없으면, 필터를 거치지 않는다.")
    @Test
    void temp2() throws ServletException, IOException {
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

    @Test
    void temp3() throws ServletException, IOException {
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

    @Test
    void temp4() throws ServletException, IOException {
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
    void when_ExcludePath_Then_ShouldNotFilter() throws ServletException, IOException {
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
