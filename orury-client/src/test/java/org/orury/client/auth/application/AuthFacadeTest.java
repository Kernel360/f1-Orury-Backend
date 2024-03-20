package org.orury.client.auth.application;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.auth.interfaces.message.AuthMessage;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.client.auth.interfaces.response.LoginResponse;
import org.orury.client.config.FacadeTest;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.auth.domain.dto.SignUpDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.orury.client.ClientFixtureFactory.TestLoginRequest.createLoginRequest;
import static org.orury.domain.DomainFixtureFactory.TestLoginDto.createLoginDto;
import static org.orury.domain.DomainFixtureFactory.TestSignUpDto.createSignUpDto;
import static org.orury.domain.DomainFixtureFactory.TestUserDto.createUserDto;

@DisplayName("[Facade] AuthFacade 테스트")
class AuthFacadeTest extends FacadeTest {

    @DisplayName("UserDto를 받아, 회원가입하고 SignUpResponse를 반환한다.")
    @Test
    void when_UserDto_Then_SignUpAndRetrieveSignUpResponse() {
        // given
        UserDto userDto = createUserDto().build().get();
        SignUpDto signUpDto = createSignUpDto().build().get();

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
        LoginRequest loginRequest = createLoginRequest().build().get();
        LoginDto loginDto = createLoginDto()
                .flag(AuthMessage.LOGIN_SUCCESS.getMessage()).build().get();

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
        LoginRequest loginRequest = createLoginRequest().build().get();
        LoginDto loginDto = createLoginDto()
                .flag(AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage()).build().get();

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
}
