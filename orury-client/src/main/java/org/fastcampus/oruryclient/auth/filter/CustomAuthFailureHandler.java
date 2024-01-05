package org.fastcampus.oruryclient.auth.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@Configuration
@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    /**
     * 이 메서드는 HTTP 요청, HTTP 응답, 그리고 인증 예외를 인자로 받는다. 인증 예외의 타입에 따라 실패 메시지를 설정하고, 이를 JSON 형태로 클라이언트에 응답한다.
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        // [STEP.1] 클라이언트로 전달할 응답값을 구성한다.
        JSONObject jsonObject = new JSONObject();
        String failMessage = "";

        // [STEP.2] 발생한 Exception에 대해서 확인한다.
        if (exception instanceof AuthenticationServiceException) {
            failMessage = "로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof LockedException) {
            failMessage = "계정이 잠겨 있습니다.";
        } else if (exception instanceof DisabledException) {
            failMessage = "계정이 비활성화되었습니다.";
        } else if (exception instanceof AccountExpiredException) {
            failMessage = "계정이 만료되었습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            failMessage = "인증 정보가 만료되었습니다.";
        }

        // [STEP.3] 응답값을 구성하고 전달한다.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (PrintWriter printWriter = response.getWriter()) {
            log.debug(failMessage);

            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("userInfo", null);
            resultMap.put("resultCode", 9999);
            resultMap.put("failMessage", failMessage);
            jsonObject = new JSONObject(resultMap);

            printWriter.print(jsonObject);
            printWriter.flush();
        }
    }

}
