package org.orury.client.crew.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.client.global.IdIdentifiable;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.global.domain.Region;

import java.time.LocalDateTime;
import java.util.List;

public record CrewsResponse(
        Long id,
        String name,
        int memberCount,
        int capacity,
        Region region,
        String icon,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt,
        List<String> tags,
        List<String> userImages
) implements IdIdentifiable {
    public static CrewsResponse of(CrewDto crewDto, List<String> userImages) {
        return new CrewsResponse(
                crewDto.id(),
                crewDto.name(),
                crewDto.memberCount(),
                crewDto.capacity(),
                crewDto.region(),
                crewDto.icon(),
                crewDto.createdAt(),
                crewDto.updatedAt(),
                crewDto.tags(),
                userImages
        );
    }
}



