package org.orury.domain.crew.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CrewStatus {
    ACTIVATED("ACTIVATED", "활성화", "A"),
    PENDING_REMOVAL("PENDING_REMOVAL", "삭제 예정", "P");

    private final String status;
    private final String description;
    private final String code;
}
