package org.orury.client.auth.application.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.orury.domain.auth.domain.dto.JwtToken;
import org.springframework.security.core.Authentication;

public interface JwtTokenService {
    Authentication getAuthenticationFromRequest(HttpServletRequest request);

    JwtToken reissueJwtTokens(HttpServletRequest request);

    JwtToken issueJwtTokens(Long id, String email);

    JwtToken issueNoUserJwtTokens(String email);
}
