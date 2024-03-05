package org.orury.client.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.orury.client.auth.interfaces.message.AuthMessage;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.client.auth.interfaces.response.LoginResponse;
import org.orury.client.auth.interfaces.response.SignUpResponse;
import org.orury.domain.auth.domain.dto.JwtToken;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.auth.domain.dto.SignUpDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {
    private final AuthService authService;

    public SignUpResponse signUp(UserDto userDto) {
        SignUpDto signUpDto = authService.signUp(userDto);
        return SignUpResponse.of(signUpDto);
    }

    public LoginResponse login(LoginRequest request) {
        LoginDto loginDto = authService.login(request);

        if (loginDto.flag().equals(AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage())) {
            return LoginResponse.fromNoUser(loginDto);
        } else {
            return LoginResponse.of(loginDto);
        }
    }

    public JwtToken reissueJwtTokens(HttpServletRequest request) {
        return authService.reissueJwtTokens(request);
    }
}
