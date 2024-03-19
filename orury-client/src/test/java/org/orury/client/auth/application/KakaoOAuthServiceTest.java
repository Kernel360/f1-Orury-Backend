package org.orury.client.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ServiceTest;
import org.orury.domain.auth.domain.dto.kakao.KakaoAccount;
import org.orury.domain.auth.domain.dto.kakao.KakaoAccountDto;
import org.orury.domain.auth.domain.dto.kakao.KakaoOAuthTokenDto;
import org.orury.domain.auth.domain.dto.kakao.Profile;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("[Service] KakaoOAuthService 테스트")
class KakaoOAuthServiceTest extends ServiceTest {

    @Test
    void getKakaoSignUpType() {
        assertEquals(KAKAO_SIGN_UP_TYPE, oAuthService.getSignUpType());
    }

    @DisplayName("OAuth인증코드를 받으면, Kakao OAuth 인증 서버에 요청을 보낸 후 응답에서 이메일 찾아 반환한다.")
    @Test
    void when_OAuthAuthenticationCode_Then_AuthenticateToKaKaoOAuthServerAndRetrieveEmail() {
        // given
        String code = "Kakao_OAuth_Authentication_Code";
        KakaoOAuthTokenDto kakaoOAuthTokenDto = createKakaoOAuthTokenDto();
        KakaoAccountDto kakaoAccountDto = createKakaoAccountDto();

        given(kakaoAuthClient.getIdToken(any(), any(), any(), any(), any()))
                .willReturn(kakaoOAuthTokenDto);
        given(kakaoKapiClient.getInfo(any(), any()))
                .willReturn(kakaoAccountDto);

        // when
        oAuthService.getEmailFromOAuthCode(code);

        // then
        then(kakaoAuthClient).should(times(1))
                .getIdToken(any(), any(), any(), any(), any());
        then(kakaoKapiClient).should(times(1))
                .getInfo(any(), any());
    }

    private static KakaoOAuthTokenDto createKakaoOAuthTokenDto() {
        return new KakaoOAuthTokenDto(
                "testAccessToken",
                "testTokenType",
                "testRefreshToken",
                "testIdToken",
                100,
                200,
                "email"
        );
    }

    private static KakaoAccountDto createKakaoAccountDto() {
        return new KakaoAccountDto(
                1L,
                true,
                LocalDateTime.of(2024, 2, 8, 22, 00),
                LocalDateTime.of(2024, 2, 8, 22, 00),
                new KakaoAccount(new Profile("testNickname"), "test@orury.com")
        );
    }
}
