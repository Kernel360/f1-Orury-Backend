package org.orury.client.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.auth.interfaces.message.AuthMessage;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.client.auth.interfaces.response.LoginResponse;
import org.orury.domain.auth.domain.dto.JwtToken;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.auth.domain.dto.SignUpDto;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Facade] AuthFacade 테스트")
@ActiveProfiles("test")
class AuthFacadeTest {
    private AuthFacade authFacade;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);

        authFacade = new AuthFacade(authService);
    }

    @DisplayName("UserDto를 받아, 회원가입하고 SignUpResponse를 반환한다.")
    @Test
    void when_UserDto_Then_SignUpAndRetrieveSignUpResponse() {
        // given
        UserDto userDto = createUserDto();
        SignUpDto signUpDto = createSignUpDto();

        given(authService.signUp(userDto))
                .willReturn(signUpDto);

        // when
        authFacade.signUp(userDto);

        // then
        then(authService).should(times(1))
                .signUp(userDto);
    }

    @DisplayName("정상 회원에 대해 LoginRequest를 받아 로그인하고, LoginResponse를 반환한다.")
    @Test
    void when_NormalUser_ThenLoginAndRetrieveLoginResponse() {
        // given
        LoginRequest loginRequest = createLoginRequest();
        LoginDto loginDto = createLoginDto(AuthMessage.LOGIN_SUCCESS);

        given(authService.login(loginRequest))
                .willReturn(loginDto);

        // when
        LoginResponse loginResponse = authFacade.login(loginRequest);

        // then
        assertFalse(loginResponse.isNoUser());

        then(authService).should(times(1))
                .login(any());
    }

    @DisplayName("비회원에 대해 LoginRequest를 받아 로그인하고, 비회원용 LoginResponse를 반환한다.")
    @Test
    void when_NoUser_ThenLoginAndRetrieveNoUserLoginResponse() {
        // given
        LoginRequest loginRequest = createLoginRequest();
        LoginDto loginDto = createLoginDto(AuthMessage.NOT_EXISTING_USER_ACCOUNT);

        given(authService.login(loginRequest))
                .willReturn(loginDto);

        // when
        LoginResponse loginResponse = authFacade.login(loginRequest);

        // then
        assertTrue(loginResponse.isNoUser());

        then(authService).should(times(1))
                .login(any());
    }

    @DisplayName("HttpServletRequest를 받아, 재발급한 토큰을 반환한다.")
    @Test
    void when_HttpServletResquest_Then_ReissueAndRetrieveJwtTokens() {
        // given
        HttpServletRequest request = new MockHttpServletRequest();

        // when
        authFacade.reissueJwtTokens(request);

        // then
        then(authService).should(times(1))
                .reissueJwtTokens(any());
    }

    private UserDto createUserDto() {
        return UserDto.of(
                1L,
                "test@test.com",
                "test",
                "password",
                1,
                1,
                LocalDate.now(),
                "test.png",
                LocalDateTime.now(),
                LocalDateTime.now(),
                NumberConstants.IS_NOT_DELETED
        );
    }

    private SignUpDto createSignUpDto() {
        return SignUpDto.of(
                createUserDto(),
                JwtToken.of("access", "refresh")
        );
    }

    private LoginRequest createLoginRequest() {
        return LoginRequest.of("code", 1);
    }

    private LoginDto createLoginDto(AuthMessage authMessage) {
        return LoginDto.of(
                createUserDto(),
                JwtToken.of("access", "refresh"),
                authMessage.getMessage()
        );
    }
}
