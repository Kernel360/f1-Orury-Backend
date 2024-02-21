package org.fastcampus.orurybatch.util;

import java.util.List;

public class Constant {
    public static final String INSERT_GYM = "INSERT INTO gym (name, road_address, address, latitude, longitude, phone_number, kakao_id, created_at, updated_at) " +
            "VALUES (:name, :roadAddress, :address, :latitude, :longitude, :phoneNumber, :kakaoId, now(), now()) " +
            "ON DUPLICATE KEY UPDATE " +
            "name = :name, " +
            "road_address = :roadAddress, " +
            "address = :address, " +
            "latitude = :latitude, " +
            "longitude = :longitude, " +
            "phone_number = :phoneNumber, " +
            "updated_at = now()";

    public static final List<String> LOCATIONS = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구",
            "광진구", "구로구", "금천구", "노원구", "도봉구",
            "동대문구", "동작구", "마포구", "서대문구", "서초구",
            "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );
}
