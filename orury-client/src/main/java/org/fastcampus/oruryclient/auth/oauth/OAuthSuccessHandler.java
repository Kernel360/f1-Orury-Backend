package org.fastcampus.oruryclient.auth.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryclient.auth.converter.message.AuthMessage;
import org.fastcampus.oruryclient.auth.converter.response.LoginResponse;
import org.fastcampus.oruryclient.auth.jwt.JwtToken;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.user.db.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");
        int signUpType = SocialProviderType.convertToIntType(provider);
        User user = oAuth2User.getAttribute("user");

        // 인증된 이메일로 조회된 계정이 없는 경우
        if (user == null) {
            LoginResponse loginResponse = LoginResponse.of(null, email, signUpType, null, null, null);

            sendResponse(
                    900,
                    AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage(), loginResponse, response);
        }

        // 인증된 이메일로 조회된 계정이 있으나, 다른 소셜로그인으로 가입한 경우
        else if (user.getSignUpType() != SocialProviderType.convertToIntType(provider)) {
            LoginResponse loginResponse = LoginResponse.of(user.getId(), email, signUpType, null, null, null);

            sendResponse(
                    910,
                    AuthMessage.NOT_MATCHING_SOCIAL_PROVIDER.getMessage(), loginResponse, response);
        }

        // 정상적으로 로그인 인증된 경우
        else {
            JwtToken jwtToken = jwtTokenProvider.createJwtToken(user.getId(), user.getEmail());
            LoginResponse loginResponse = LoginResponse.of(user.getId(), email, signUpType, user.getNickname(), jwtToken.accessToken(), jwtToken.refreshToken());

            sendResponse(
                    HttpStatus.OK.value(),
                    AuthMessage.LOGIN_SUCCESS.getMessage(), loginResponse, response);
        }
    }

    private void sendResponse(int status, String message, LoginResponse loginResponse, HttpServletResponse response) throws IOException {
        String json = new ObjectMapper().writeValueAsString(
                ApiResponse.builder()
                        .status(status)
                        .message(message)
                        .data(loginResponse)
                        .build()
        );

        response.setCharacterEncoding("UTF-16");
        response.getWriter().write(json);
    }
}
