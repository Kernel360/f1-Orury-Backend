package org.orury.domain.user.infrastucture;

import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
