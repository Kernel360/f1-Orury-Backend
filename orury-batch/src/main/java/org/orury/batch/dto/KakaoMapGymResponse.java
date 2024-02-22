package org.orury.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMapGymResponse {
    private List<Documents> documents;

    private Meta meta;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
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
    public static class Meta {
        private Boolean isEnd;
        private Integer pageableCount;
        private SameName sameName;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SameName {
            private String keyword;
            private List<String> region;
            private String selectedRegion;
        }

        private Integer totalCount;
    }
}
