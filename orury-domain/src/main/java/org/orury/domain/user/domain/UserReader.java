package org.orury.domain.user.domain;

import org.orury.domain.user.domain.entity.User;

import java.util.Optional;
import java.util.List;

public interface UserReader {
    Optional<User> findUserById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User getUserById(Long userId);
}
