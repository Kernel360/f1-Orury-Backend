package org.orury.domain.crew.domain.dto;

import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewTag;

public record CrewTagDto(
        Long id,
        Crew crew,
        String tag
) {
    public static CrewTagDto of(
            Long id,
            Crew crew,
            String tag
    ) {
        return new CrewTagDto(
                id,
                crew,
                tag
        );
    }

    public static CrewTagDto from(CrewTag crewTag) {
        return CrewTagDto.of(
                crewTag.getId(),
                crewTag.getCrew(),
                crewTag.getTag()
        );
    }

    public CrewTag toEntity() {
        return CrewTag.of(
                id,
                crew,
                tag
        );
    }
}
