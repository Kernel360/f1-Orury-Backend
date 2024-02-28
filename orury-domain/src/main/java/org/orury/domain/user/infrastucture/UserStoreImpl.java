package org.orury.domain.user.infrastucture;

import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.entity.User;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {
    private final UserRepository userRepository;

    @Override
    public void save(User entity) {
        userRepository.save(entity);
    }


}
