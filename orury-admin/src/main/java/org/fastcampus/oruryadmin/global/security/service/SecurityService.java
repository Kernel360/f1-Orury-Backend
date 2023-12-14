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

/**
 * 관리자 인증을 위한 UserDetailsService 구현체
 * Spring Security에서 인증을 위해 사용하는 UserDetailsService를 구현한 클래스
 * loadUserByUsername 메서드를 오버라이딩하여 사용자 정보를 가져오는 로직을 구현
 * loadUserByUsername 메서드는 사용자의 아이디를 파라미터로 받아서 UserDetails 타입의 객체를 리턴
 * UserDetails 타입의 객체는 사용자의 정보와 권한 정보를 담고 있음
 * UserDetails 타입의 객체를 리턴하는 이유는 Spring Security에서 제공하는 User 클래스를 사용하지 않고
 * 직접 만든 AuthenticationAdmin 클래스를 사용하기 위함
 * AuthenticationAdmin 클래스는 Admin 클래스를 상속받아서 만든 클래스로
 * Admin 클래스에는 사용자의 정보와 권한 정보가 담겨 있음
 */
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