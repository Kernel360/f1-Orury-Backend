package org.orury.client.crew.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.global.domain.Region;

import java.time.LocalDateTime;
import java.util.List;

public record CrewResponse(
        Long id,
        String name,
        String headName,
        int memberCount,
        int capacity,
        Region region,
        String description,
        String icon,
        CrewStatus status,
        String headProfileImage,
        String question,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt,
        List<String> tags,
        boolean isMember,
        List<String> userImages
) {
    public static CrewResponse of(CrewDto crewDto, boolean isMember, List<String> userImages) {
        return new CrewResponse(
                crewDto.id(),
                crewDto.name(),
                crewDto.userDto().nickname(),
                crewDto.memberCount(),
                crewDto.capacity(),
                crewDto.region(),
                crewDto.description(),
                crewDto.icon(),
                crewDto.status(),
                crewDto.userDto().profileImage(),
                crewDto.question(),
                crewDto.createdAt(),
                crewDto.updatedAt(),
                crewDto.tags(),
                isMember,
                userImages
        );
    }
}
