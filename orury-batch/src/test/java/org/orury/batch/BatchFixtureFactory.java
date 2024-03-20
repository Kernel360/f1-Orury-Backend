package org.orury.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.batch.dto.GymResponse;

public class BatchFixtureFactory {

    private BatchFixtureFactory() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestGymResponse {
        private @Builder.Default String placeName = "placeName";
        private @Builder.Default String kakaoId = "kakaoId";
        private @Builder.Default String roadAddressName = "roadAddressName";
        private @Builder.Default String phone = "phone";
        private @Builder.Default String y = "y";
        private @Builder.Default String x = "x";
        private @Builder.Default String addressName = "addressName";

        public static TestGymResponse.TestGymResponseBuilder createGymResponse() {
            return TestGymResponse.builder();
        }

        public GymResponse get() {
            return mapper.convertValue(this, GymResponse.class);
        }
    }
}
