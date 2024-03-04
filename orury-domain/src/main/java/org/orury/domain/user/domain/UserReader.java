package org.orury.domain.user.domain;

import org.orury.domain.user.domain.entity.User;

import java.util.Optional;

public interface UserReader {
    Optional<User> findUserById(Long id);
}
