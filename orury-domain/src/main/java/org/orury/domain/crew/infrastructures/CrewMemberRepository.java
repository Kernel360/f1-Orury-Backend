package org.orury.domain.crew.infrastructures;

import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewMemberRepository extends JpaRepository<CrewMember, CrewMemberPK> {

    List<CrewMember> findByCrewMemberPK_UserId(Long userId);
}
