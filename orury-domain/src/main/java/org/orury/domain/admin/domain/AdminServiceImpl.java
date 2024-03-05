package org.orury.domain.admin.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.PostErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.admin.domain.dto.AdminDto;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminReader adminReader;
    private final UserReader userReader;
    private final UserStore userStore;

    @Override
    public AdminDto findAdminByEmail(String email) {
        var admin = adminReader.findByEmail(email);
        var dto = AdminDto.from(admin);
        log.debug("admin : {}", admin);
        log.debug("dto : {}", dto);
        return dto;
    }

    @Override
    public List<UserDto> getUsers() {
        return userReader.findAll().stream()
                .map(UserDto::from)
                .toList();
    }

    @Override
    public UserDto getUserDtoById(Long userId) {
        return userReader.findUserById(userId)
                .map(UserDto::from)
                .orElseThrow(() -> new BusinessException(PostErrorCode.NOT_FOUND));
    }

    @Override
    public void deleteUser(Long userId) {
        userStore.delete(userId);
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
