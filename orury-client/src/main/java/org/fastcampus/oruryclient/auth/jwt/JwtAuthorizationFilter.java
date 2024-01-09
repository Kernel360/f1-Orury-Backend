package org.fastcampus.oruryclient.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter {

//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        String header = request.getHeader("accessToken");
//
//        // 헤더가 없거나 Bearer 토큰이 아닌 경우
//        if (header == null || !header.startsWith("Bearer ")) {
//            log.error("Error occurs while getting header. header is null or invalid {}", request.getRequestURL());
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 토큰 추출
//        String accessToken = header.split(" ")[1].trim();
//
//        // 토큰이 유효한 경우
//        if (jwtTokenProvider.validateAccessToken(accessToken)) {
//            Authentication authentication = jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String[] excludePath = {"/api/signin", "/api/refresh"};
//        String path = request.getRequestURI();
//        return Arrays.stream(excludePath).anyMatch(path::startsWith);
//    }
}
