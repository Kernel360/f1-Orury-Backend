package org.fastcampus.orurydomain.user.db.repository;

import org.fastcampus.orurydomain.user.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);

    User findByEmailAndSignUpType(String email, int signUpType);
}
