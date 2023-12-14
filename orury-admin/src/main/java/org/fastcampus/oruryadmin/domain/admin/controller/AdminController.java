package org.fastcampus.oruryadmin.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.admin.converter.dto.AdminDto;
import org.fastcampus.oruryadmin.global.security.dto.login.request.LoginRequest;
import org.fastcampus.oruryadmin.global.security.dto.login.response.LoginResponse;
import org.fastcampus.oruryadmin.domain.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/{adminId}")
    public AdminDto getAdminById(
            @PathVariable Long adminId
    ) {
        return adminService.findAdminById(adminId);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = adminService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }


}
