package org.fastcampus.oruryapi.domain.user.db.repository;

import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndSignUpType(String email, int signUpType);
}
