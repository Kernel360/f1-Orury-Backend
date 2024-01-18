package org.fastcampus.orurydomain.user.db.repository;

import org.fastcampus.orurydomain.user.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
