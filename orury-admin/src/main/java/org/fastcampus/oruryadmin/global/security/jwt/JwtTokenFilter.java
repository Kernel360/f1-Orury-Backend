package org.fastcampus.oruryadmin.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * JWT 토큰 Filter
 * OncePerRequestFilter: 요청 당 한번만 필터링
 * doFilterInternal: 필터링 로직,
 * shouldNotFilter: 필터링 하지 않을 요청
 */
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("accessToken");

        // 헤더가 없거나 Bearer 토큰이 아닌 경우
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Error occurs while getting header. header is null or invalid {}", request.getRequestURL());
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String accessToken = header.split(" ")[1].trim();

        // 토큰이 유효한 경우
        if (jwtTokenProvider.validateAccessToken(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/signin", "/api/refresh"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}