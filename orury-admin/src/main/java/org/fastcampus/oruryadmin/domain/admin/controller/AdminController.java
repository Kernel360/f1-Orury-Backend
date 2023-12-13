package org.fastcampus.oruryadmin.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.admin.converter.dto.AdminDto;
import org.fastcampus.oruryadmin.domain.admin.converter.request.RequestAdmin;
import org.fastcampus.oruryadmin.domain.admin.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController("/api")
public class AdminController {
    private final AdminService adminService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{adminId}")
    public AdminDto getAdminById(
            @PathVariable Long adminId
    ) {
        return adminService.findAdminById(adminId);
    }

    @PostMapping("/signup")
    public AdminDto signup(
            @RequestBody RequestAdmin request
    ) {
        return adminService.signup(request);
    }
}
