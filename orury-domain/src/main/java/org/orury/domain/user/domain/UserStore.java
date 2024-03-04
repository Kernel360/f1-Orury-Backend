package org.orury.domain.user.domain;

import org.orury.domain.user.domain.entity.User;

public interface UserStore {
    void save(User entity);

    void saveAndFlush(User user);
}
