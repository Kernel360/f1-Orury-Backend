package org.fastcampus.orurybatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurybatch.dto.KakaoMapGymResponse;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GymItemReader implements ItemReader<KakaoMapGymResponse> {
    private final KakaoMapClient kakaoMapClient;
    private List<String> locations = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구",
            "광진구", "구로구", "금천구", "노원구", "도봉구",
            "동대문구", "동작구", "마포구", "서대문구", "서초구",
            "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );

    @Override
    public KakaoMapGymResponse read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("GymItemReader.read()");

        var response = kakaoMapClient.searchGyms("구로구");
//        boolean isEnd = response.getMeta().getIs_end();
//        var item = response.getDocuments().stream()
//                .map(it -> GymResponse.of(
//                                it.getPlace_name(),
//                                it.getRoad_address_name(),
//                                it.getPhone(),
//                                it.getX(),
//                                it.getY(),
//                                it.getAddress_name(),
//                                isEnd
//                        )
//                )
//                .toList();
//        if (response.getMeta().getIs_end()) break;

//        log.info("item reader items: {}", item);
        return response;
    }
}
