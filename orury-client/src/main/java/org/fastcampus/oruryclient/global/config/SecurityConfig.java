package org.fastcampus.oruryclient.global.config;

import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryclient.auth.filter.CustomAuthenticationFilter;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                antMatcher("/auth/**"),
                                antMatcher("/swagger-ui/**"),
                                antMatcher("/swagger-resources/**"),
                                PathRequest.toStaticResources().atCommonLocations()
                        ).permitAll()
                )
                .addFilterAt(new CustomAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    // CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "https://f1-orury-client.vercel.app/")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


//    private AuthorizationDecision isAdmin(
//            Supplier<Authentication> authenticationSupplier,
//            RequestAuthorizationContext requestAuthorizationContext
//    ) {
//        return new AuthorizationDecision(
//                authenticationSupplier.get()
//                        .getAuthorities()
//                        .contains(new SimpleGrantedAuthority("ADMIN"))
//        );
//    }

//    @Bean
//    public UserDetailsService userDetailsService(UserService service) {
//        return username -> service
//                .getUser(username)
//                .map(UserPrincipal::from)
//                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
//    }

//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
//            AuthService service,
//            PasswordEncoder encoder
//    ) {
//        final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();
//        return userRequest -> {
//            OAuth2User user = defaultService.loadUser(userRequest);
//            KakaoResponse kakaoResponse = KakaoResponse.from(user.getAttributes());
//            String email = kakaoResponse.email();
//            String dummyPassword = encoder.encode("{bcrypt}" + UUID.randomUUID());
//            String nickname = kakaoResponse.nickname();
//            return service.getUserInfo(email)
//                    .map(UserPrincipal::from)
//                    .orElseGet(() ->
//                            UserPrincipal.from(
//                                    service.saveUser(email, dummyPassword, nickname)
//                            ));
//        };
//    }
}
