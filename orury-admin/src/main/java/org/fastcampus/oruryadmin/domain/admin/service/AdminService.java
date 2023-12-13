package org.fastcampus.oruryadmin.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.admin.converter.dto.AdminDto;
import org.fastcampus.oruryadmin.domain.admin.converter.request.RequestAdmin;
import org.fastcampus.oruryadmin.domain.admin.db.model.Admin;
import org.fastcampus.oruryadmin.domain.admin.db.repository.AdminRepository;
import org.fastcampus.oruryadmin.util.SecurityUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public Optional<AdminDto> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email).map(AdminDto::from);
    }

    @Transactional(readOnly = true)
    public AdminDto findAdminById(Long id) {
        return adminRepository.findById(id)
                .map(AdminDto::from)
                .orElseThrow();
    }

    public AdminDto signup(RequestAdmin request) {
        Admin admin = adminRepository.save(Admin.of(request.name(), request.email(), request.password()));
        return AdminDto.from(admin);
    }

    @Transactional(readOnly = true)
    public AdminDto getMyUserWithAuthorities() {
        return AdminDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(adminRepository::findOneWithAuthoritiesByName)
                        .orElseThrow(() -> new UsernameNotFoundException("Admin not found"))
        );
    }

}
