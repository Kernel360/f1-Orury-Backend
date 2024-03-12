package org.orury.domain.global.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Region {
    강남구("서울 강남구"),
    강동구("서울 강동구"),
    강북구("서울 강북구"),
    강서구("서울 강서구"),
    관악구("서울 관악구"),
    광진구("서울 광진구"),
    구로구("서울 구로구"),
    금천구("서울 금천구"),
    노원구("서울 노원구"),
    도봉구("서울 도봉구"),
    동대문구("서울 동대문구"),
    동작구("서울 동작구"),
    마포구("서울 마포구"),
    서대문구("서울 서대문구"),
    서초구("서울 서초구"),
    성동구("서울 성동구"),
    성북구("서울 성북구"),
    송파구("서울 송파구"),
    양천구("서울 양천구"),
    영등포구("서울 영등포구"),
    용산구("서울 용산구"),
    은평구("서울 은평구"),
    종로구("서울 종로구"),
    중구("서울 중구"),
    중랑구("서울 중랑구");

    private final String description;

    @JsonCreator
    public static Region getEnumFromValue(String value) {
        for (Region region : Region.values()) {
            if (region.name().equals(value)) {
                return region;
            }
        }
        return null;
    }
}
