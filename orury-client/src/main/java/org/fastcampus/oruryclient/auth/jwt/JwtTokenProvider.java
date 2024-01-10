package org.fastcampus.oruryclient.auth.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private SecretKey secretKey;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret) {


        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

//    @Value("${jwt.token-validity-in-seconds}")
//    private long accessExpirationTime;
//    private final long refreshExpirationTime = 86400000L * 30L; // 30일
//    private final Key key;
//
//    @Autowired
//    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // JWT 토큰 생성
//    public JwtToken createJwtToken(String email, String authorities) {
//
//        Claims claims = Jwts.claims().setSubject(email);
//        claims.put("roles", authorities);
//
//        String accessToken = Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTime))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        String refreshToken = Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//
//        return JwtToken.of(accessToken, refreshToken);
//    }
//
//    // JWT 토큰에서 인증 정보 조회
//    public boolean validateAccessToken(String accessToken) {
//        try {
//            parseToken(accessToken);
//            return true;
//        } catch (final JwtException | IllegalArgumentException exception) {
//            return false;
//        }
//    }
//
//    private Claims parseToken(final String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    //해당 부분은 저도 잘 모릅니다 ㅠㅠ
//    // 토큰 정보 조회하고 권한을 추출해서 Authentication 객체를 리턴하는 메서드인 것 같습니다.
//    public Authentication getAuthenticationByAccessToken(String accessToken) {
//
//        Claims claims = parseToken(accessToken);
//
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get("roles").toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
}
