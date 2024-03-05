package org.orury.domain.admin.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.domain.admin.infrastructure.AdminRepository;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminStoreImpl implements AdminStore {
    private final AdminRepository adminRepository;

}
