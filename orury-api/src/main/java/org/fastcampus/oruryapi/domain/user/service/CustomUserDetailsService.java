package org.fastcampus.oruryapi.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보를 조회하는 로직을 구현합니다.
        // 실제로는 DB에서 사용자 정보를 조회해야 하지만, 여기서는 간단하게 처리하기 위해 직접 생성한 사용자 정보를 반환합니다.
        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        return User.builder().username(username).password(password).authorities("ROLE_USER").build();
    }
}