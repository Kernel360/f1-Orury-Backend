package org.orury.client.auth.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.client.auth.application.jwt.JwtTokenService;
import org.orury.common.error.code.ErrorResponse;
import org.orury.common.error.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            Authentication authentication = jwtTokenService.getAuthenticationFromRequest(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthException e) {
            jwtExceptionHandler(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/auth/login", "/auth/refresh", "/swagger-ui", "/v3/api-docs", "/favicon.ico", "/actuator/prometheus"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath)
                .anyMatch(path::contains);
    }

    private void jwtExceptionHandler(HttpServletResponse response, AuthException exception) {
        response.setStatus(exception.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(exception.getStatus(), exception.getMessage()));
            response.getWriter()
                    .write(json);
            log.error("### Error Occurred in JwtTokenFilter : {}", exception.getMessage(), exception);
        } catch (Exception e) {
            log.error("### Error Occurred in JwtTokenFilter : {}", exception.getMessage(), exception);
        }
    }
}
