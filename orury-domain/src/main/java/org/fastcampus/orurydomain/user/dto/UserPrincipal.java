package org.fastcampus.orurydomain.user.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record UserPrincipal(
        Long id,
        String email,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {
    public static UserPrincipal of(
            Long id,
            String email,
            Collection<? extends GrantedAuthority> authorities
    ) {
        return new UserPrincipal(
                id,
                email,
                authorities
        );
    }

    public static UserPrincipal fromToken(Long id, String email, String role) {
        // SimpleGrantedAuthority를 사용하여 주어진 role로 권한 리스트를 생성합니다.
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // 생성자의 마지막 인자로 authorities를 전달하여 UserPrincipal 인스턴스를 생성합니다.
        return UserPrincipal.of(
                id,
                email,
                authorities // 생성한 권한 리스트
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * User 클래스 상속을 받지 않아 실질적으로 필요가 없다.
     * 게정 만료 여부,
     * 계정 lock 여부,
     * 인증 만료 여부,
     * 활성 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


