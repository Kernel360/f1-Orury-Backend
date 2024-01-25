package org.fastcampus.oruryclient.auth.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.auth.converter.message.AuthMessage;
import org.fastcampus.oruryclient.auth.converter.request.LoginRequest;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.orurycommon.error.code.AuthErrorCode;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.fastcampus.orurydomain.auth.dto.JwtToken;
import org.fastcampus.orurydomain.auth.dto.LoginDto;
import org.fastcampus.orurydomain.auth.dto.kakao.KakaoAccountDto;
import org.fastcampus.orurydomain.auth.dto.kakao.KakaoTokenDto;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleLoginStrategy implements LoginStrategy {
    private final int SIGN_UP_TYPE = 2;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String kakaoRedirectURI;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String kakaoClientSecret;

    @Override
    public LoginDto login(LoginRequest request) {
        String code = request.code();
        int signUpType = request.signUpType();

        String kakaoToken = getKakaoToken(code).accessToken();
        String email = getEmailFromToken(kakaoToken);

        // 카카오 이메일이 없는 고객인 경우
        if (email == null) {
            throw new AuthException(AuthErrorCode.NO_EMAIL);
        }
        Optional<User> user = userRepository.findByEmail(email);
        // 비회원인 경우
        if (user.isEmpty()) {
            return LoginDto.of(null, null, AuthMessage.NOT_EXISTING_USER_ACCOUNT.getMessage());
        }
        return getLoginDto(user.get());
    }

    @Override
    public int getSignUpType() {
        return SIGN_UP_TYPE;
    }

    private KakaoTokenDto getKakaoToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        // Http Response Body 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", kakaoClientId); // 카카오 Dev 앱 REST API 키
        params.add("redirect_uri", kakaoRedirectURI); // 카카오 Dev redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값
        params.add("client_secret", kakaoClientSecret); // 카카오 Dev 카카오 로그인 Client Secret

        // 헤더와 바디 합치기 위해 Http Entity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오로부터 Access token 받아오기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (-> KakaoTokenDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoTokenDto kakaoTokenDto = null;
        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;
    }

    public String getEmailFromToken(String kakaoAccessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 후 response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        // JSON Parsing (-> kakaoAccountDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoAccountDto kakaoAccountDto = null;
        try {
            kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoAccountDto.kakaoAccount().email();
    }

    private LoginDto getLoginDto(User user) {
        UserDto userDto = UserDto.from(user);

        // 다른 소셜 로그인으로 가입한 회원인 경우
        if (userDto.signUpType() != SIGN_UP_TYPE) {
            return LoginDto.of(null, null, AuthMessage.NOT_MATCHING_SOCIAL_PROVIDER.getMessage());
        }

        JwtToken jwtToken = jwtTokenProvider.issueJwtTokens(userDto.id(), userDto.email());
        return LoginDto.of(userDto, jwtToken, AuthMessage.LOGIN_SUCCESS.getMessage());
    }
}
