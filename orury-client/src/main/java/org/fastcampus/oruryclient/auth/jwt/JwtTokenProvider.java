package org.fastcampus.oruryclient.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.global.constants.Constants;
import org.fastcampus.orurycommon.error.code.TokenErrorCode;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    static final long ACCESS_TOKEN_EXPIRATION_TIME = 300000L; // 5분 (1000 * 60 * 5)
    static final long REFRESH_TOKEN_EXPIRATION_TIME = 1200000L; // 20분 (1000 * 60 * 20)

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String accessTokenHeader = request.getHeader("accessToken");

        // AccessToken 헤더가 없거나 Bearer 토큰이 아닌 경우
        if (accessTokenHeader == null || !accessTokenHeader.startsWith("Bearer ")) {
            throw new AuthException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }

        // AccessToken 추출
        return accessTokenHeader.split(" ")[1].trim();
    }

    public Authentication getAuthenticationFromAccessToken(String accessToken) {
        Claims claims;

        try {
            claims = parseToken(accessToken);
        } catch (final JwtException | IllegalArgumentException exception) {
            log.error("Error parsing token: {}", exception.getMessage());
            throw new AuthException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Constants.ROLE_USER.getMessage()));

        UserPrincipal userDetails = UserPrincipal.fromToken((long) (int) claims.get("id"), claims.getSubject(), Constants.ROLE_USER.getMessage());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public JwtToken reissueJwtTokens(String refreshToken) {
        // Refresh 토큰 헤더가 없거나 Bearer 토큰이 아닌 경우
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new AuthException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }

        // Refresh 토큰 추출
        refreshToken = refreshToken.split(" ")[1].trim();

        // Refresh 토큰 검증
        Claims claims;
        try {
            claims = parseToken(refreshToken);
        } catch (final JwtException | IllegalArgumentException exception) {
            log.error("Error parsing token: {}", exception.getMessage());
            throw new AuthException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        // Access 토큰, Refresh 토큰 모두 재발급
        String createdAccessToken = createAccessToken((long) (int) claims.get("id"), claims.getSubject());
        String reissuedRefreshToken = reissueRefreshToken((long) (int) claims.get("id"), claims.getSubject(), claims.getIssuedAt(), claims.getExpiration());

        return JwtToken.of(createdAccessToken, reissuedRefreshToken);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtToken createJwtToken(UserDto userDto) {
        String accessToken = createAccessToken(userDto.id(), userDto.email());
        String refreshToken = createRefreshToken(userDto.id(), userDto.email());

        return JwtToken.of(accessToken, refreshToken);
    }

    private String createAccessToken(Long id, String email) {
        return "Bearer " + Jwts.builder()
                .subject(email)
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    private String createRefreshToken(Long id, String email) {
        return "Bearer " + Jwts.builder()
                .subject(email)
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    private String reissueRefreshToken(Long id, String email, Date issuedAt, Date expiredAt) {
        return "Bearer " + Jwts.builder()
                .subject(email)
                .claim("id", id)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }
}
