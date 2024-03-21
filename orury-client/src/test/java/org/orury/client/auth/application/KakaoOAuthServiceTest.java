package org.orury.client.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.client.config.ServiceTest;
import org.orury.domain.auth.domain.dto.kakao.KakaoAccountDto;
import org.orury.domain.auth.domain.dto.kakao.KakaoOAuthTokenDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.orury.domain.AuthDomainFixture.TestKakaoAccountDto.createKakaoAccountDto;
import static org.orury.domain.AuthDomainFixture.TestKakaoOAuthTokenDto.createKakaoOAuthTokenDto;

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
        KakaoOAuthTokenDto kakaoOAuthTokenDto = createKakaoOAuthTokenDto().build().get();
        KakaoAccountDto kakaoAccountDto = createKakaoAccountDto().build().get();

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
}
