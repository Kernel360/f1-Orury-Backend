package org.orury.client.crew.application;

import lombok.RequiredArgsConstructor;
import org.orury.common.error.code.CrewErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.crew.domain.CrewReader;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {
    private final CrewReader crewReader;


    @Override
    @Transactional(readOnly = true)
    public CrewDto getCrewDtoById(Long crewId) {
        Crew crew = crewReader.findById(crewId)
                .orElseThrow(() -> new BusinessException(CrewErrorCode.NOT_FOUND));
        return CrewDto.from(crew);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByRank(Pageable pageable) {
        return crewReader.getCrewsByRank(pageable)
                .map(CrewDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByRecommend(Pageable pageable) {
        return crewReader.getCrewsByRecommend(pageable)
                .map(CrewDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewDto> getCrewDtosByUserId(Long userId, Pageable pageable) {
        return crewReader.getCrewsByUserId(userId, pageable)
                .map(CrewDto::from);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existCrewMember(CrewMemberPK crewMemberPK) {
        return crewReader.existCrewMember(crewMemberPK);
    }

}
