package org.orury.domain.crew.infrastructures;

import lombok.RequiredArgsConstructor;
import org.orury.domain.crew.domain.CrewApplicationStore;
import org.orury.domain.crew.domain.dto.CrewApplicationDto;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.CrewApplicationPK;
import org.orury.domain.crew.domain.entity.CrewMember;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrewApplicationStoreImpl implements CrewApplicationStore {
    private final CrewApplicationRepository crewApplicationRepository;
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;

    @Override
    public void save(CrewDto crewDto, UserDto userDto, String answer) {
        CrewApplicationDto crewApplicationDto = CrewApplicationDto.of(crewDto, userDto, answer);
        crewApplicationRepository.save(crewApplicationDto.toEntity());
    }

    @Override
    public void approve(Long crewId, Long userId) {
        CrewApplicationPK crewApplicationPK = CrewApplicationPK.of(userId, crewId);
        this.approve(crewApplicationPK);
    }

    @Override
    public void approve(CrewApplicationPK crewApplicationPK) {
        crewApplicationRepository.deleteById(crewApplicationPK);

        Long userId = crewApplicationPK.getUserId();
        Long crewId = crewApplicationPK.getCrewId();
        CrewMember crewMember = CrewMember.of(CrewMemberPK.of(userId, crewId), null, null);
        crewMemberRepository.save(crewMember);
        crewRepository.increaseMemberCount(crewId);
    }

    @Override
    public void delete(Long crewId, Long userId) {
        CrewApplicationPK crewApplicationPK = CrewApplicationPK.of(userId, crewId);
        crewApplicationRepository.deleteById(crewApplicationPK);
    }

    @Override
    public void delete(CrewApplicationPK crewApplicationPK) {
        crewApplicationRepository.deleteById(crewApplicationPK);
    }
}
