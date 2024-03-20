package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.auth.domain.dto.JwtToken;
import org.orury.domain.auth.domain.dto.LoginDto;
import org.orury.domain.auth.domain.dto.SignUpDto;
import org.orury.domain.auth.domain.dto.kakao.KakaoAccount;
import org.orury.domain.auth.domain.dto.kakao.KakaoAccountDto;
import org.orury.domain.auth.domain.dto.kakao.KakaoOAuthTokenDto;
import org.orury.domain.auth.domain.dto.kakao.Profile;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;


public class AuthDomainFixture {

    private AuthDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestJwtToken {
        private @Builder.Default String accessToken = "testAccessToken";
        private @Builder.Default String refreshToken = "testRefreshToken";

        public static TestJwtToken.TestJwtTokenBuilder createJwtToken() {
            return TestJwtToken.builder();
        }

        public JwtToken get() {
            return mapper.convertValue(this, JwtToken.class);
        }
    }

    @Getter
    @Builder
    public static class TestSignUpDto {
        private @Builder.Default UserDto userDto = UserDomainFixture.TestUserDto.createUserDto().build().get();
        private @Builder.Default JwtToken jwtToken = TestJwtToken.createJwtToken().build().get();

        public static TestSignUpDto.TestSignUpDtoBuilder createSignUpDto() {
            return TestSignUpDto.builder();
        }

        public SignUpDto get() {
            return mapper.convertValue(this, SignUpDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestLoginDto {
        private @Builder.Default UserDto userDto = UserDomainFixture.TestUserDto.createUserDto().build().get();
        private @Builder.Default JwtToken jwtToken = TestJwtToken.createJwtToken().build().get();
        private @Builder.Default String flag = "testFlag";

        public static TestLoginDto.TestLoginDtoBuilder createLoginDto() {
            return TestLoginDto.builder();
        }

        public LoginDto get() {
            return mapper.convertValue(this, LoginDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestKakaoOAuthTokenDto {
        private @Builder.Default String accessToken = "testAccessToken";
        private @Builder.Default String tokenType = "testTokenType";
        private @Builder.Default String refreshToken = "testRefreshToken";
        private @Builder.Default String idToken = "testIdToken";
        private @Builder.Default int expiresIn = 100;
        private @Builder.Default int refreshTokenExpiresIn = 200;
        private @Builder.Default String scope = "email";

        public static TestKakaoOAuthTokenDto.TestKakaoOAuthTokenDtoBuilder createKakaoOAuthTokenDto() {
            return TestKakaoOAuthTokenDto.builder();
        }

        public KakaoOAuthTokenDto get() {
            return mapper.convertValue(this, KakaoOAuthTokenDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestKakaoAccountDto {
        private @Builder.Default Long id = 1L;
        private @Builder.Default boolean hasSignedUp = true;
        private @Builder.Default LocalDateTime connectedAt = LocalDateTime.of(2024, 2, 8, 22, 00);
        private @Builder.Default LocalDateTime synchedAt = LocalDateTime.of(2024, 2, 8, 22, 00);
        private @Builder.Default KakaoAccount kakaoAccount = new KakaoAccount(new Profile("testNickname"), "test@orury.com");

        public static TestKakaoAccountDto.TestKakaoAccountDtoBuilder createKakaoAccountDto() {
            return TestKakaoAccountDto.builder();
        }

        public KakaoAccountDto get() {
            return mapper.convertValue(this, KakaoAccountDto.class);
        }
    }
}
