package org.orury.domain.admin.domain;

import org.orury.domain.admin.domain.entity.Admin;

import java.util.List;

public interface AdminReader {
    Admin findByEmail(String email);

    Admin findById(Long id);

    List<Admin> findAll();
}
