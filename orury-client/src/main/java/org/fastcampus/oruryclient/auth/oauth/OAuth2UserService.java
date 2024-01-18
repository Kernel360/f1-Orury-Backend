package org.fastcampus.oruryclient.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryclient.global.constants.Constants;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)를 가져온다.
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.from(
                provider,
                oAuth2User.getAttributes()
        );

        // OAuth2Attribute의 속성값들을 Map으로 반환 받는다.
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        // 이메일로 가입된 회원이 있다면 memberAttribute에 넣어준다.
        String email = (String) memberAttribute.get("email");
        User user = userRepository.findByEmail(email).orElse(null);
        memberAttribute.put("user", user);

        // 회원의 권한과, 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Constants.ROLE_USER.getMessage())),
                memberAttribute,
                "email");
    }
}
