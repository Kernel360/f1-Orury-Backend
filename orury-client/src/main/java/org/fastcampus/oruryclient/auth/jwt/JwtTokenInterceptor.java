package org.fastcampus.oruryclient.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.auth.error.JwtErrorCode;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {

        // favicon.ico 요청에 대한 JWT 토큰 검증을 건너뛰기
        if (request.getRequestURI().equals("/favicon.ico")) {
            return true;
        }

        String token = null;

        // 쿠키에서 JWT 토큰 가져오기
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            if (TokenUtils.isValidToken(token)) {
                String userId = TokenUtils.getUserIdFromToken(token);
                if (userId == null) {
                    log.debug("token isn't userId");
                    throw new BusinessException(JwtErrorCode.AUTH_TOKEN_NOT_MATCH);
                }
                return true;
            } else {
                throw new BusinessException(JwtErrorCode.TOKEN_NOT_VALID);
            }
        } else {
            throw new BusinessException(JwtErrorCode.AUTH_TOKEN_IS_NULL);
        }
    }


}
