package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewMemberReader;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.user.domain.entity.User;
import org.orury.domain.user.infrastucture.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CrewMemberReaderImpl implements CrewMemberReader {
    private final CrewMemberRepository crewMemberRepository;
    private final UserRepository userRepository;

    @Override
    public boolean existByCrewIdAndUserId(Long crewId, Long userId) {
        return crewMemberRepository.existsByCrewMemberPK_CrewIdAndCrewMemberPK_UserId(crewId, userId);
    }

    @Override
    public int countByUserId(Long userId) {
        return crewMemberRepository.countByCrewMemberPK_UserId(userId);
    }

    @Override
    public List<User> getMembersByCrewId(Long crewId) {
        List<CrewMember> crewMembers = crewMemberRepository.findByCrewMemberPK_CrewId(crewId);
        return crewMembers.stream()
                .map(crewMember -> crewMember.getCrewMemberPK().getUserId())
                .map(userRepository::findUserById)
                .toList();
    }
}
