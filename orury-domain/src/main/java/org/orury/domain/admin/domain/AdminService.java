package org.orury.domain.admin.domain;

import org.orury.domain.admin.domain.dto.AdminDto;

import java.util.List;

public interface AdminService {
    AdminDto getAdmin(Long adminId);

    List<AdminDto> getAdmins();

    AdminDto findAdminByEmail(String email);
}
