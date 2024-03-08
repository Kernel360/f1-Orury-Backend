package org.orury.domain.user.infrastucture;

import org.orury.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findUserById(Long id);
}
