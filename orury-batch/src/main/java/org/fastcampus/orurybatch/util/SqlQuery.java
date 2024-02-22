package org.fastcampus.orurybatch.util;

public class SqlQuery {
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
}
