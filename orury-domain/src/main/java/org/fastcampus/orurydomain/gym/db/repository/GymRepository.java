package org.fastcampus.orurydomain.gym.db.repository;

import org.fastcampus.orurydomain.gym.db.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long> {
}