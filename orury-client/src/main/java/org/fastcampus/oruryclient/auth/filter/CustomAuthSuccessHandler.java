package org.fastcampus.oruryclient.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.auth.jwt.TokenUtils;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        log.debug("3.CustomLoginSuccessHandler");

        // 1. 사용자와 관련된 정보를 모두 조회한다.
        UserPrincipal userPrincipal = ((UserPrincipal) authentication.getPrincipal());

        // 2. 조회한 데이터를 JSONObject 형태로 파싱한다.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = objectMapper.writeValueAsString(userPrincipal);
        JSONObject userPrincipalObject = new JSONObject(objectMapper.writeValueAsString(userPrincipal));

        HashMap<String, Object> responseMap = new HashMap<>();
        JSONObject jsonObject;

        // 1. 일반 계정일 경우 데이터 세팅
        responseMap.put("userInfo", 1234);
        responseMap.put("resultCode", 200);
        responseMap.put("failMessage", null);
        jsonObject = new JSONObject(responseMap);

        // JWT 토큰 생성
        String token = TokenUtils.generateJwtToken(userPrincipal);
        jsonObject.put("token", token);

        // 쿠키에 JWT 토큰 저장
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true); // JavaScript에서 쿠키에 접근할 수 없도록 설정
        jwtCookie.setPath("/"); // 모든 경로에서 쿠키에 접근 가능하도록 설정
        response.addCookie(jwtCookie); // 응답에 쿠키 추가


        // 4. 구성한 응답값을 전달한다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(jsonObject); // 최종 저장된 '사용자 정보', '사이트 정보'를 Front에 전달
            printWriter.flush();
        }
    }

}