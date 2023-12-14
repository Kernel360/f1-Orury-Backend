package org.fastcampus.oruryadmin.global.security.service;

import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryadmin.global.security.dto.AuthenticationAdmin;
import org.fastcampus.oruryadmin.domain.admin.db.model.Admin;
import org.fastcampus.oruryadmin.domain.admin.db.repository.AdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("member not found!"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(admin.getRole().name()));

        return new AuthenticationAdmin(admin, authorities);
    }
}