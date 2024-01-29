package org.fastcampus.orurybatch.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoMapGymResponse {
    private List<Documents> documents;

    private Meta meta;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Documents {
        private String addressName;
        private String categoryGroupCode;
        private String categoryGroupName;
        private String categoryName;
        private String distance;
        private String id;
        private String phone;
        private String placeName;
        private String placeUrl;
        private String roadAddressName;
        private String x;
        private String y;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Meta {
        private Boolean isEnd;
        private Integer pageableCount;
        private SameName sameName;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class SameName {
            private String keyword;
            private List<String> region;
            private String selectedRegion;
        }

        private Integer totalCount;
    }
}
