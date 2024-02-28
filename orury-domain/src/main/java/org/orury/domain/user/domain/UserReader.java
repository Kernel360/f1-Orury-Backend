package org.orury.domain.user.domain;

import org.orury.domain.user.domain.entity.User;

public interface UserReader {
    User findUserById(Long id);
}
