package org.orury.domain.crew.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.crew.domain.entity.CrewTag;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.orury.domain.CrewDomainFixture.TestCrewTag.createCrewTag;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Reader] 크루태그 ReaderImpl 테스트")
@ActiveProfiles("test")
class CrewTagReaderImplTest extends InfrastructureTest {

    @DisplayName("크루id를 받아, 크루태그 목록을 조회한다.")
    @Test
    void getTagsByCrewId() {
        // given
        List<CrewTag> crewTags = List.of(
                createCrewTag().tag("tag1").build().get(),
                createCrewTag().tag("tag2").build().get(),
                createCrewTag().tag("tag3").build().get()
        );
        given(crewTagRepository.findCrewTagByCrewId(anyLong()))
                .willReturn(crewTags);

        // when
        List<String> tags = crewTagReader.getTagsByCrewId(anyLong());

        // then
        assertThat(tags).containsExactly("tag1", "tag2", "tag3");
        then(crewTagRepository).should(only())
                .findCrewTagByCrewId(anyLong());
    }
}
