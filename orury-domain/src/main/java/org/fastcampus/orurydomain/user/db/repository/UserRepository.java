package org.fastcampus.oruryclient.domain.user.db.repository;

import org.fastcampus.oruryclient.domain.user.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUserById(Long id);

    Optional<User> findByEmailAndSignUpType(String email, int signUpType);
}
