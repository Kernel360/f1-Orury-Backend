package org.orury.domain.user.infrastucture;

import lombok.RequiredArgsConstructor;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {
    private final UserRepository userRepository;

    @Override
    public void save(User entity) {
        userRepository.save(entity);
    }

    @Override
    public Class<? extends Throwable> saveAndFlush(User user) {
        userRepository.saveAndFlush(user);
        return null;
    }


}
