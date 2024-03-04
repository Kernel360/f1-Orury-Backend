package org.orury.admin.global.security.dto.login.response;

import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.admin.domain.dto.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record AdminPrincipal(
        Long id,
        String name,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User {
    public static AdminPrincipal of(
            Long id,
            String name,
            String email,
            String password
    ) {
        return AdminPrincipal.of(
                id,
                name,
                email,
                password,
                Map.of()
        );
    }

    public static AdminPrincipal of(
            Long id,
            String name,
            String email,
            String password,
            Map<String, Object> oAuth2Attributes
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
                        .collect(Collectors.toSet()),
                oAuth2Attributes
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

    public AdminDto toDto() {

        return AdminDto.of(
                id,
                name,
                email,
                password,
                Set.of(RoleType.ADMIN, RoleType.USER)
        );
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
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

    @Override
    public String getName() {
        return email;
    }
}
