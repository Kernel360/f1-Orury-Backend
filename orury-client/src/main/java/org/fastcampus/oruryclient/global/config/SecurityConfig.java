package org.fastcampus.oruryclient.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

//    @Autowired
//    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                antMatcher("/**"),
                                antMatcher("/auth/**"),
                                antMatcher("/swagger-ui/**"),
                                antMatcher("/swagger-resources/**"),
                                antMatcher("/post/**"),
                                antMatcher("/posts/**"),
                                PathRequest.toH2Console(),
                                PathRequest.toStaticResources().atCommonLocations()
                        ).permitAll()
                )

                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

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
