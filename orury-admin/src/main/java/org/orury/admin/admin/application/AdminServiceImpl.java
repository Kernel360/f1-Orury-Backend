package org.orury.admin.admin.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.admin.domain.AdminReader;
import org.orury.domain.admin.domain.dto.AdminDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminReader adminReader;

    @Override
    public AdminDto findAdminByEmail(String email) {
        var admin = adminReader.findByEmail(email);
        return AdminDto.from(admin);
    }

    @Override
    public AdminDto getAdmin(Long id) {
        return AdminDto.from(adminReader.findById(id));
    }

    @Override
    public List<AdminDto> getAdmins() {
        return adminReader.findAll().stream().map(AdminDto::from).toList();
    }
}
