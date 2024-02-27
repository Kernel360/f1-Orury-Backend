package org.orury.client.auth.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.TokenErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.domain.auth.db.model.RefreshToken;
import org.orury.domain.auth.db.repository.RefreshTokenRepository;
import org.orury.domain.auth.dto.JwtToken;
import org.orury.domain.global.constants.Constants;
import org.orury.domain.user.dto.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_HEADER_NAME = "Authorization";

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7L; // 7일
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14L; // 14일

    // 비회원 전용 토큰
    private static final long NO_USER_ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30L; // 30분

    private final SecretKey secretKey;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret, RefreshTokenRepository refreshTokenRepository) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(TOKEN_HEADER_NAME);

        // AccessToken 헤더가 없거나 Bearer 토큰이 아닌 경우
        if (accessTokenHeader == null || !accessTokenHeader.startsWith(JWT_TOKEN_PREFIX)) {
            log.error("### Error with token header: Authorization does not exist or Token is not bearer token");
            throw new AuthException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        }

        // AccessToken 추출
        return accessTokenHeader.split(" ")[1].trim();
    }

    public Authentication getAuthenticationFromAccessToken(String accessToken) {
        Claims claims;

        try {
            claims = parseToken(accessToken);
        } catch (final MalformedJwtException | IllegalArgumentException exception) {
            throw new AuthException(TokenErrorCode.INVALID_ACCESS_TOKEN);
        } catch (final ExpiredJwtException exception) {
            if (Objects.nonNull(exception.getClaims().get("email"))) {
                throw new AuthException(TokenErrorCode.EXPIRED_NO_USER_TOKEN);
            }
            throw new AuthException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (final JwtException exception) {
            throw new AuthException(TokenErrorCode.EXPIRED_ACCESS_TOKEN);
        }

        // 비회원은 토큰에 email 필드가 있으므로, 해당 필드로 검증 가능
        if (claims.get("email") != null) {
            // 임시 Authentication 세팅
            UserPrincipal userDetails = UserPrincipal.fromToken(0L, claims.get("email").toString(), Constants.ROLE_USER.getMessage());
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Constants.ROLE_USER.getMessage()));

            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        }

        UserPrincipal userDetails = UserPrincipal.fromToken((long) (int) claims.get("id"), claims.getSubject(), Constants.ROLE_USER.getMessage());
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Constants.ROLE_USER.getMessage()));

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public JwtToken reissueJwtTokens(HttpServletRequest request) {
        String refreshTokenHeader = request.getHeader(TOKEN_HEADER_NAME);
        // Refresh 토큰 헤더가 없거나 Bearer 토큰이 아닌 경우
        if (refreshTokenHeader == null || !refreshTokenHeader.startsWith(JWT_TOKEN_PREFIX)) {
            throw new AuthException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Refresh 토큰 추출
        String refreshToken = refreshTokenHeader.split(" ")[1].trim();

        // Refresh 토큰 검증
        Claims claims;
        try {
            claims = parseToken(refreshToken);
        } catch (final MalformedJwtException | IllegalArgumentException exception) {
            log.error("### Error when parsing token: {}", exception.getMessage());
            throw new AuthException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        } catch (final JwtException exception) {
            log.error("### Error when parsing token: {}", exception.getMessage());
            throw new AuthException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        if (!refreshTokenRepository.existsByValue(refreshToken))
            throw new AuthException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);

        // Access 토큰, Refresh 토큰 모두 재발급
        return issueJwtTokens((long) (int) claims.get("id"), claims.getSubject());
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtToken issueJwtTokens(Long id, String email) {
        String accessToken = createJwtToken(id, email, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = createJwtToken(id, email, REFRESH_TOKEN_EXPIRATION_TIME);

        RefreshToken newRefreshToken = RefreshToken.of(id, refreshToken, LocalDateTime.now(), LocalDateTime.now());
        refreshTokenRepository.save(newRefreshToken);

        return JwtToken.of(accessToken, refreshToken);
    }

    public JwtToken issueNoUserJwtTokens(String email) {
        String accessToken = noUserCreateJwtToken(email);

        return JwtToken.of(accessToken, null);
    }

    private String createJwtToken(Long id, String email, long expirationTime) {
        return Jwts.builder()
                .subject(email)
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    private String noUserCreateJwtToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JwtTokenProvider.NO_USER_ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }
}
