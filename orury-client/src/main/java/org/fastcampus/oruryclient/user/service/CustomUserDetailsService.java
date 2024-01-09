package org.fastcampus.oruryclient.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.user.error.UserErrorCode;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.fastcampus.orurydomain.user.dto.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // 1. userRepository로부터 loginId로 유저정보를 받아온다.
        User byLoginId = userRepository.findByEmail(loginId)
                .orElseThrow(
                        () -> new BusinessException(UserErrorCode.NOT_FOUND)
                );

        // 2.user를 dto로 변환시켜준다.
        UserDto userDto = UserDto.from(byLoginId);
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER") // 예시 권한
        );

        // 3. 사용자 정보를 기반으로 SecurityUserDetailsDto 객체를 생성한다.
        return new UserPrincipal(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                userDto.password(),
                authorities
        );
    }

}