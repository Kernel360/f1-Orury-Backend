package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.Crew;

public interface CrewStore {
    Crew save(Crew crew);

    void delete(Crew crew);
}
