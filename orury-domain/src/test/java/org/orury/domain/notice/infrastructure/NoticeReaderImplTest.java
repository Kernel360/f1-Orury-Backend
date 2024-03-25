package org.orury.domain.notice.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orury.domain.config.InfrastructureTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@DisplayName("[Reader] 공지 ReaderImpl 테스트")
class NoticeReaderImplTest extends InfrastructureTest {

    @Test
    void getNotice() {
        // given & when
        noticeReader.getNotice(258L);

        // then
        then(noticeRepository).should(only())
                .findById(anyLong());
    }

    @Test
    void getNotices() {
        // given & when
        noticeReader.getNotices();

        // then
        then(noticeRepository).should(only())
                .findAll();
    }
}