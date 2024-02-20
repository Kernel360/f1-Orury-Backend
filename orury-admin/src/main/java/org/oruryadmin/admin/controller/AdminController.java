package org.oruryadmin.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oruryadmin.admin.service.AdminService;
import org.orurydomain.admin.dto.AdminDto;
import org.oruryadmin.global.security.dto.login.request.LoginRequest;
import org.oruryadmin.global.security.dto.login.response.LoginResponse;
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
