package org.orury.domain.user.domain;

import org.orury.domain.user.domain.entity.User;

public interface UserStore {
    void save(User entity);

    Class<? extends Throwable> saveAndFlush(User user);

    void delete(Long userId);
}
