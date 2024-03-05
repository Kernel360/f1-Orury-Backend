package org.orury.domain.admin.domain;

import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.user.domain.dto.UserDto;

import java.util.List;

public interface AdminService {
    AdminDto getAdmin(Long adminId);

    List<AdminDto> getAdmins();

    AdminDto findAdminByEmail(String email);

    List<UserDto> getUsers();

    Object getUserDtoById(Long userId);

    void deleteUser(Long userId);
}
