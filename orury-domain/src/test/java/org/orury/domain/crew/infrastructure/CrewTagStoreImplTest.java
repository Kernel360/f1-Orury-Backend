package org.orury.domain.crew.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.crew.domain.entity.Crew;
import org.orury.domain.crew.domain.entity.CrewTag;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.orury.domain.CrewDomainFixture.TestCrew.createCrew;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Store] 크루태그 StoreImpl 테스트")
@ActiveProfiles("test")
class CrewTagStoreImplTest extends InfrastructureTest {

    @DisplayName("크루와 태그리스트를 받아, 태그를 추가한다.")
    @Test
    void addTags() {
        // given
        Crew crew = createCrew().build().get();
        List<String> tags = List.of("tag1", "tag2", "tag3", "tag4");

        // when
        crewTagStore.addTags(crew, tags);

        // then
        then(crewTagRepository).should(times(tags.size()))
                .save(any(CrewTag.class));
    }

    @DisplayName("기존 태그리스트와 새로운 태그리스트, 크루를 받아, 태그를 업데이트한다.")
    @Test
    void updateTags() {
        // given
        Crew crew = createCrew().build().get();
        List<String> oldTags = List.of("tag1", "tag2", "tag3");
        List<String> newTags = List.of("tag1", "tag2", "tag3", "tag4");

        // when
        crewTagStore.updateTags(oldTags, newTags, crew);

        // then
        then(crewTagRepository).should(times(1))
                .deleteAllByCrewId(crew.getId());
        then(crewTagRepository).should(times(newTags.size()))
                .save(any(CrewTag.class));
    }

    @DisplayName("기존 태그리스트와 새로운 태그리스트가 같다면, 아무것도 하지 않는다.")
    @Test
    void updateTags_sameTags() {
        // given
        Crew crew = createCrew().build().get();
        List<String> oldTags = List.of("tag1", "tag2", "tag3");
        List<String> newTags = List.of("tag1", "tag2", "tag3");

        // when
        crewTagStore.updateTags(oldTags, newTags, crew);

        // then
        then(crewTagRepository).shouldHaveNoInteractions();
    }
}
