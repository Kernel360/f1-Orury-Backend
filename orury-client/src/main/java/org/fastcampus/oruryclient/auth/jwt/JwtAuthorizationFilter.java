package org.fastcampus.oruryclient.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.fastcampus.oruryclient.auth.error.AuthErrorCode;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.global.error.code.UserErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. 토큰이 필요하지 않은 API URL에 대해서 배열로 구성한다.
        List<String> list = Arrays.asList(
                "/auth/login"  // 로그인 페이지의 URL을 추가합니다.
        );

        // 2. 토큰이 필요하지 않은 API URL의 경우 -> 로직 처리없이 다음 필터로 이동한다.
        if (list.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. OPTIONS 요청일 경우 -> 로직 처리 없이 다음 필터로 이동
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // [STEP.1] Client에서 API를 요청할때 쿠키를 확인한다.
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        try {
            // [STEP.2-1] 쿠키 내에 토큰이 존재하는 경우
            if (token != null && !token.equalsIgnoreCase("")) {

                // [STEP.2-2] 쿠키 내에있는 토큰이 유효한지 여부를 체크한다.
                if (TokenUtils.isValidToken(token)) {

                    // [STEP.2-3] 추출한 토큰을 기반으로 사용자 아이디를 반환받는다.
                    String loginId = TokenUtils.getUserIdFromToken(token);
                    log.debug("[+] loginId Check: " + loginId);

                    // [STEP.2-4] 사용자 아이디가 존재하는지에 대한 여부를 체크한다.
                    if (loginId != null && !loginId.equalsIgnoreCase("")) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(request, response);
                    } else {
                        throw new BusinessException(UserErrorCode.NOT_FOUND);
                    }
                }
                // [STEP.2-5] 토큰이 유효하지 않은 경우
                else {
                    throw new BusinessException(AuthErrorCode.TOKEN_NOT_VALID);
                }
            }
            // [STEP.3] 토큰이 존재하지 않는 경우
            else {
                throw new BusinessException(AuthErrorCode.TOKEN_NOT_FOUND);
            }
        } catch (Exception e) {
            // 로그 메시지 생성
            String logMessage = jsonResponseWrapper(e).getAsString("message");
            log.error(logMessage, e);  // 로그에만 해당 메시지를 출력합니다.

            // 클라이언트에게 전송할 고정된 메시지
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", true);
            jsonObject.put("message", "로그인 에러");

            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }


    /**
     * 토큰 관련 Exception 발생 시 예외 응답값 구성
     */
    private JSONObject jsonResponseWrapper(Exception e) {

        String resultMessage = "";
        // JWT 토큰 만료
        if (e instanceof ExpiredJwtException) {
            resultMessage = "TOKEN Expired";
        }
        // JWT 허용된 토큰이 아님
        else if (e instanceof BusinessException) {
            resultMessage = "TOKEN SignatureException Login";
        }
        // JWT 토큰내에서 오류 발생 시
        else if (e instanceof BusinessException) {
            resultMessage = "TOKEN Parsing JwtException";
        }
        // 이외 JTW 토큰내에서 오류 발생
        else {
            resultMessage = "OTHER TOKEN ERROR";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "9999");
        jsonMap.put("message", resultMessage);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        log.error(resultMessage, e);
        return jsonObject;
    }

}
