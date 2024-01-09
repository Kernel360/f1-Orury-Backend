package org.fastcampus.orurydomain.gym.db.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurydomain.base.db.AuditingField;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity(name = "gym")
public class Gym extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "score_average")
    private Float scoreAverage;

    @Column(name = "images")
    private String images;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "open_time")
    private String openTime;

    @Column(name = "close_time")
    private String closeTime;

    @Column(name = "brand")
    private String brand;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "setting_day")
    private String settingDay;

    private Gym(
            Long id,
            String name,
            String kakaoId,
            String roadAddress,
            String address,
            Float scoreAverage,
            String images,
            String latitude,
            String longitude,
            String openTime,
            String closeTime,
            String brand,
            String phoneNumber,
            String instagramLink,
            String settingDay
    ) {
        this.id = id;
        this.name = name;
        this.kakaoId = kakaoId;
        this.roadAddress = roadAddress;
        this.address = address;
        this.scoreAverage = scoreAverage;
        this.images = images;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.brand = brand;
        this.phoneNumber = phoneNumber;
        this.instagramLink = instagramLink;
        this.settingDay = settingDay;
    }

    public static Gym of(
            Long id,
            String name,
            String kakaoId,
            String roadAddress,
            String address,
            Float scoreAverage,
            String images,
            String latitude,
            String longitude,
            String openTime,
            String closeTime,
            String brand,
            String phoneNumber,
            String instagramLink,
            String settingDay
    ) {
        return new Gym(
                id,
                name,
                kakaoId,
                roadAddress,
                address,
                scoreAverage,
                images,
                latitude,
                longitude,
                openTime,
                closeTime,
                brand,
                phoneNumber,
                instagramLink,
                settingDay
        );
    }
}