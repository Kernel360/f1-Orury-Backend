package org.orury.admin.global.security.dto.login.response;

import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.admin.domain.dto.RoleType;
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
        var roleTypes = Set.of(RoleType.USER, RoleType.ADMIN);
        return new AdminPrincipal(
                id,
                name,
                email,
                password,
                roleTypes.stream()
                        .map(RoleType::getRoleName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
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
