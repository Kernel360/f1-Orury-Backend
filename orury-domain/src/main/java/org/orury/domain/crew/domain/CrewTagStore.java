package org.orury.domain.crew.domain;

import org.orury.domain.crew.domain.entity.Crew;

import java.util.List;

public interface CrewTagStore {
    void addTags(Crew crew, List<String> tags);

    void updateTags(List<String> oldTags, List<String> newTags, Crew crew);
}
