package org.orury.admin.global.security.dto;

import org.orury.domain.admin.db.model.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthenticationAdmin extends User {

    public AuthenticationAdmin(Admin admin, Collection<? extends GrantedAuthority> authorities) {
        super(admin.getEmail(), admin.getPassword(), authorities);
    }

}