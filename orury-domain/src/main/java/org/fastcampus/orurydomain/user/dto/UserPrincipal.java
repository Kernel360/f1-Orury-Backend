package org.fastcampus.orurydomain.user.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record UserPrincipal(
        Long id,
        String email,
        String nickname,
        String password,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {
    public static UserPrincipal of(
            Long id,
            String email,
            String nickname,
            String password
    ) {
        return UserPrincipal.of(
                id,
                email,
                nickname,
                password
        );
    }

    public static UserPrincipal from(UserDto userDto) {
        return UserPrincipal.of(
                userDto.id(),
                userDto.email(),
                userDto.nickname(),
                userDto.password()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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


