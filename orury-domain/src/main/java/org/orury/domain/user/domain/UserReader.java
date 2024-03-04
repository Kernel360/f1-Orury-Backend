package org.orury.domain.user.domain;

import org.orury.domain.user.domain.entity.User;

import java.util.Optional;

public interface UserReader {
    User findUserById(Long id);

    Optional<User> findByEmail(String email);
}
