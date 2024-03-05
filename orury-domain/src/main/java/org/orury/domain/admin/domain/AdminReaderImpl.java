package org.orury.domain.admin.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.admin.domain.entity.Admin;
import org.orury.domain.admin.infrastructure.AdminRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminReaderImpl implements AdminReader {
    private final AdminRepository adminRepository;

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("어드민 오류"));
    }

    @Override
    public Admin findById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new RuntimeException("아이디 오류"));
    }

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }
}
