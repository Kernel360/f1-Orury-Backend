package org.fastcampus.oruryclient.auth.oauth;

import java.util.HashMap;
import java.util.Map;

public record OAuth2Attribute(
        String email,
        String provider
) {
    private static OAuth2Attribute of(String email, String provider) {
        return new OAuth2Attribute(email, provider);
    }

    // 서비스에 따라 OAuth2Attribute 객체를 생성하는 메서드
    static OAuth2Attribute from(
            String provider,
            Map<String, Object> attributes
    ) {
        if (provider.toLowerCase().equals(SocialProviderType.KAKAO.getProviderName()))
            return fromKakao(provider, "email", attributes);
        return null;
    }

    /*
     *   Kakao 로그인일 경우 사용하는 메서드, 필요한 사용자 정보가 kakaoAccount -> kakaoProfile 두번 감싸져 있어서,
     *   두번 get() 메서드를 이용해 사용자 email 조회.
     * */
    private static OAuth2Attribute fromKakao(
            String provider,
            String attributeKey,
            Map<String, Object> attributes
    ) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2Attribute.of((String) kakaoAccount.get(attributeKey), provider);
    }

    // OAuth2User 객체에 넣어주기 위해서 Map으로 값들을 반환한다.
    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }
}
