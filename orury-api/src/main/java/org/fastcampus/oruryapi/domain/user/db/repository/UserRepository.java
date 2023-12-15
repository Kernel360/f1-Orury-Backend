package org.fastcampus.oruryapi.domain.user.db.repository;

import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserById(Long id);
}
