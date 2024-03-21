package org.orury.domain.notice.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.notice.domain.entity.Notice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

@DisplayName("[Store] 공지 StoreImpl 테스트")
class NoticeStoreImplTest extends InfrastructureTest {

    @Test
    void delete() {
        // given & when
        noticeStore.delete(2389L);

        // then
        then(noticeRepository).should(only())
                .deleteById(anyLong());
    }

    @Test
    void save() {
        // given & when
        noticeStore.save(mock(Notice.class));

        // then
        then(noticeRepository).should(only())
                .save(any(Notice.class));
    }
}