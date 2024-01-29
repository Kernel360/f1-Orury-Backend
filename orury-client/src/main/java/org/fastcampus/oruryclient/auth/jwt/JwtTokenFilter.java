package org.fastcampus.oruryclient.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.error.code.ErrorResponse;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = null;
        Authentication authentication = null;
        try {
            accessToken = jwtTokenProvider.getTokenFromRequest(request);
            authentication = jwtTokenProvider.getAuthenticationFromAccessToken(accessToken);
        } catch (AuthException e) {
            jwtExceptionHandler(response, e);
            return;
        }

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/v1/auth", "/swagger-ui", "/v3/api-docs", "/favicon.ico", "/actuator/prometheus"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath)
                .anyMatch(path::startsWith);
    }

    private void jwtExceptionHandler(HttpServletResponse response, AuthException exception) {
        response.setStatus(exception.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(exception.getStatus(), exception.getMessage()));
            response.getWriter()
                    .write(json);
            log.warn(exception.getMessage(), exception);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
