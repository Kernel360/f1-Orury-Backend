package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLike;
import org.orury.domain.gym.domain.entity.GymLikePK;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;

public class GymDomainFixture {

    private GymDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestGym {
        private @Builder.Default Long id = 734136L;
        private @Builder.Default String kakaoId = "암장카카오아이디";
        private @Builder.Default String name = "암장이름";
        private @Builder.Default String roadAddress = "암장도로명주소";
        private @Builder.Default String address = "암장지번주소";
        private @Builder.Default float totalScore = 42.5f;
        private @Builder.Default int reviewCount = 22;
        private @Builder.Default int likeCount = 13;
        private @Builder.Default List<String> images = List.of();
        private @Builder.Default double latitude = 124.456;
        private @Builder.Default double longitude = 124.456;
        private @Builder.Default String brand = "짐브랜드";
        private @Builder.Default String phoneNumber = "010-2234-5678";
        private @Builder.Default String instagramLink = "인스타주소";
        private @Builder.Default String settingDay = "TUESDAY";
        private @Builder.Default String serviceMon = "12:00-23:11";
        private @Builder.Default String serviceTue = "13:00-23:22";
        private @Builder.Default String serviceWed = "14:00-23:33";
        private @Builder.Default String serviceThu = "11:00-23:44";
        private @Builder.Default String serviceFri = "12:00-23:55";
        private @Builder.Default String serviceSat = "13:00-23:66";
        private @Builder.Default String serviceSun = "14:00-23:77";
        private @Builder.Default String homepageLink = "암장홈페이지";
        private @Builder.Default String remark = "암장주석";
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestGym.TestGymBuilder createGym() {
            return TestGym.builder();
        }

        public static TestGym.TestGymBuilder createGym(Long gymId) {
            return TestGym.builder().id(gymId);
        }

        public Gym get() {
            return mapper.convertValue(this, Gym.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymDto {
        private @Builder.Default Long id = 82875L;
        private @Builder.Default String name = "gymKakaoId";
        private @Builder.Default String kakaoId = "gymName";
        private @Builder.Default String roadAddress = "gymRoadAddress";
        private @Builder.Default String address = "gymAddress";
        private @Builder.Default float totalScore = 40.5f;
        private @Builder.Default int reviewCount = 23;
        private @Builder.Default int likeCount = 12;
        private @Builder.Default List<String> images = List.of("gymImage");
        private @Builder.Default double latitude = 123.456;
        private @Builder.Default double longitude = 123.456;
        private @Builder.Default String brand = "gymBrand";
        private @Builder.Default String phoneNumber = "010-1234-5678";
        private @Builder.Default String instagramLink = "gymInstaLink";
        private @Builder.Default String settingDay = "MONDAY";
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();
        private @Builder.Default EnumMap<DayOfWeek, String> businessHours = new EnumMap<>(DayOfWeek.class);
        private @Builder.Default String homepageLink = "gymHomepageLink";
        private @Builder.Default String remark = "gymRemark";

        public static TestGymDto.TestGymDtoBuilder createGymDto() {
            return TestGymDto.builder();
        }

        public static TestGymDto.TestGymDtoBuilder createGymDto(Long gymId) {
            return TestGymDto.builder().id(gymId);
        }

        public GymDto get() {
            return mapper.convertValue(this, GymDto.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymLikePK {
        private @Builder.Default Long gymId = 9634L;
        private @Builder.Default Long userId = 2128L;

        public static TestGymLikePK.TestGymLikePKBuilder createGymLikePK() {
            return TestGymLikePK.builder();
        }

        public GymLikePK get() {
            return mapper.convertValue(this, GymLikePK.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymLike {
        private @Builder.Default GymLikePK gymLikePK = TestGymLikePK.createGymLikePK().build().get();

        public static TestGymLike.TestGymLikeBuilder createGymLike() {
            return TestGymLike.builder();
        }

        public static TestGymLike.TestGymLikeBuilder createGymLike(Long gymId, Long userId) {
            return TestGymLike.builder().gymLikePK(
                    TestGymLikePK.createGymLikePK()
                            .gymId(gymId)
                            .userId(userId).build().get()
            );
        }

        public GymLike get() {
            return mapper.convertValue(this, GymLike.class);
        }
    }

    @Getter
    @Builder
    public static class TestGymLikeDto {
        private @Builder.Default GymLikePK gymLikePK = TestGymLikePK.createGymLikePK().build().get();

        public static TestGymLikeDto.TestGymLikeDtoBuilder createGymLikeDto() {
            return TestGymLikeDto.builder();
        }

        public static TestGymLikeDto.TestGymLikeDtoBuilder createGymLikeDto(GymLikePK gymLikePK) {
            return TestGymLikeDto.builder().gymLikePK(gymLikePK);
        }

        public static TestGymLikeDto.TestGymLikeDtoBuilder createGymLikeDto(Long gymId, Long userId) {
            return TestGymLikeDto.builder()
                    .gymLikePK(TestGymLikePK.createGymLikePK()
                            .gymId(gymId)
                            .userId(userId).build().get()
                    );
        }

        public GymLikeDto get() {
            return mapper.convertValue(this, GymLikeDto.class);
        }
    }
}
