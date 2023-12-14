package org.fastcampus.oruryadmin.domain.admin.converter.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record AdminPrincipal(
        Long id,
        String name,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {
    public static AdminPrincipal of(
            Long id,
            String name,
            String email,
            String password
    ) {
        Set<RoleType> roleTypes = Set.of(RoleType.ROLE_ADMIN);

        return new AdminPrincipal(
                id,
                name,
                email,
                password,
                roleTypes.stream().map(RoleType::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableSet()));
    }

    public static AdminPrincipal from(AdminDto dto) {
        return AdminPrincipal.of(
                dto.id(),
                dto.name(),
                dto.email(),
                dto.password()
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
