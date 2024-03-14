package org.orury.domain.crew.domain;

import java.util.List;

public interface CrewTagReader {
    List<String> getTagsByCrewId(Long crewId);
}
