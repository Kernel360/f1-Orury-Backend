package org.orury.admin.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.admin.global.security.dto.login.request.LoginRequest;
import org.orury.admin.global.security.dto.login.response.LoginResponse;
import org.orury.admin.global.security.jwt.JwtToken;
import org.orury.admin.global.security.jwt.JwtTokenProvider;
import org.orury.domain.admin.db.repository.AdminRepository;
import org.orury.domain.admin.dto.AdminDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public Optional<AdminDto> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email).map(AdminDto::from);
    }

    public AdminDto findAdminById(Long id) {
        return adminRepository.findById(id)
                .map(AdminDto::from)
                .orElseThrow();
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        JwtToken jwtToken = jwtTokenProvider.createJwtToken(request.email(), authorities);
        return LoginResponse.of(jwtToken);
    }
}
