package org.orury.domain.user.domain;

import org.orury.domain.user.domain.entity.User;

import java.util.List;

public interface UserReader {
    User findUserById(Long id);

    List<User> findAll();
}
