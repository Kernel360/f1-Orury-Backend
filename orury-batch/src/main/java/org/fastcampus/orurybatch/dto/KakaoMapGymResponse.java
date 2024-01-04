package org.fastcampus.orurybatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


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
        private String address_name;
        private String category_group_code;
        private String category_group_name;
        private String category_name;
        private String distance;
        private String id;
        private String phone;
        private String place_name;
        private String place_url;
        private String road_address_name;
        private String x;
        private String y;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private Boolean is_end;
        private Integer pageable_count;
        private Same_name same_name;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Same_name {
            private String keyword;
            private List<String> region;
            private String selected_region;
        }

        private Integer total_count;
    }
}
