package org.oruryclient.config;

import org.orurydomain.global.constants.Constants;
import org.orurydomain.user.dto.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.List;

public class WithUserPrincipalSecurityContextFactory implements WithSecurityContextFactory<WithUserPrincipal> {
    @Override
    public SecurityContext createSecurityContext(WithUserPrincipal annotation) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Constants.ROLE_USER.getMessage()));
        UserPrincipal userPrincipal = new UserPrincipal(annotation.id(), annotation.email(), authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, "", authorities);
        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
