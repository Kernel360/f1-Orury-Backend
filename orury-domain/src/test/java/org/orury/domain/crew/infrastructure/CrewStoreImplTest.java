package org.orury.domain.crew.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.crew.domain.entity.Crew;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Store] 크루 StoreImpl 테스트")
@ActiveProfiles("test")
class CrewStoreImplTest extends InfrastructureTest {

    @DisplayName("크루를 받아 저장한다.")
    @Test
    void save() {
        // given & when
        crewStore.save(mock(Crew.class));

        // then
        then(crewRepository).should(only())
                .save(any(Crew.class));
    }

    @DisplayName("크루를 받아 삭제한다.")
    @Test
    void delete() {
        // given & when
        crewStore.delete(mock(Crew.class));

        // then
        then(crewRepository).should(only())
                .delete(any(Crew.class));
    }
}
