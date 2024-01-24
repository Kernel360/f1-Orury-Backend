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

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "images")
    private String images;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "brand")
    private String brand;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "setting_day")
    private String settingDay;

    @Column(name = "service_mon")
    private String serviceMon;

    @Column(name = "service_tue")
    private String serviceTue;

    @Column(name = "service_wed")
    private String serviceWed;

    @Column(name = "service_thu")
    private String serviceThu;

    @Column(name = "service_fri")
    private String serviceFri;

    @Column(name = "service_sat")
    private String serviceSat;

    @Column(name = "service_sun")
    private String serviceSun;

    @Column(name = "homepage_link")
    private String homepageLink;

    @Column(name = "remark")
    private String remark;

    private Gym(
            Long id,
            String name,
            String kakaoId,
            String roadAddress,
            String address,
            Float scoreAverage,
            int likeCount,
            String images,
            String latitude,
            String longitude,
            String brand,
            String phoneNumber,
            String instagramLink,
            String settingDay,
            String serviceMon,
            String serviceTue,
            String serviceWed,
            String serviceThu,
            String serviceFri,
            String serviceSat,
            String serviceSun,
            String homepageLink,
            String remark
    ) {
        this.id = id;
        this.name = name;
        this.kakaoId = kakaoId;
        this.roadAddress = roadAddress;
        this.address = address;
        this.scoreAverage = scoreAverage;
        this.likeCount = likeCount;
        this.images = images;
        this.latitude = latitude;
        this.longitude = longitude;
        this.brand = brand;
        this.phoneNumber = phoneNumber;
        this.instagramLink = instagramLink;
        this.settingDay = settingDay;
        this.serviceMon = serviceMon;
        this.serviceTue = serviceTue;
        this.serviceWed = serviceWed;
        this.serviceThu = serviceThu;
        this.serviceFri = serviceFri;
        this.serviceSat = serviceSat;
        this.serviceSun = serviceSun;
        this.homepageLink = homepageLink;
        this.remark = remark;
    }

    public static Gym of(
            Long id,
            String name,
            String kakaoId,
            String roadAddress,
            String address,
            Float scoreAverage,
            int likeCount,
            String images,
            String latitude,
            String longitude,
            String brand,
            String phoneNumber,
            String instagramLink,
            String settingDay,
            String serviceMon,
            String serviceTue,
            String serviceWed,
            String serviceThu,
            String serviceFri,
            String serviceSat,
            String serviceSun,
            String homepageLink,
            String remark
    ) {
        return new Gym(
                id,
                name,
                kakaoId,
                roadAddress,
                address,
                scoreAverage,
                likeCount,
                images,
                latitude,
                longitude,
                brand,
                phoneNumber,
                instagramLink,
                settingDay,
                serviceMon,
                serviceTue,
                serviceWed,
                serviceThu,
                serviceFri,
                serviceSat,
                serviceSun,
                homepageLink,
                remark
        );
    }
}