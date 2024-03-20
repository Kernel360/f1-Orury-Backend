package org.orury.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.client.auth.interfaces.request.LoginRequest;
import org.orury.client.comment.interfaces.request.CommentCreateRequest;
import org.orury.client.comment.interfaces.request.CommentUpdateRequest;
import org.orury.client.crew.interfaces.request.CrewRequest;
import org.orury.client.meeting.interfaces.request.MeetingCreateRequest;
import org.orury.client.meeting.interfaces.request.MeetingUpdateRequest;
import org.orury.client.review.interfaces.request.ReviewCreateRequest;
import org.orury.client.review.interfaces.request.ReviewReactionRequest;
import org.orury.client.review.interfaces.request.ReviewUpdateRequest;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.global.domain.Region;

import java.time.LocalDateTime;
import java.util.List;

public class ClientFixtureFactory {

    private ClientFixtureFactory() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestCommentCreateRequest {
        private @Builder.Default String content = "댓글생성내용";
        private @Builder.Default Long parentId = 81402L;
        private @Builder.Default Long postId = 115360L;

        public static TestCommentCreateRequest.TestCommentCreateRequestBuilder createCommentCreateRequest() {
            return TestCommentCreateRequest.builder();
        }

        public CommentCreateRequest get() {
            return mapper.convertValue(this, CommentCreateRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestCommentUpdateRequest {
        private @Builder.Default Long id = 981410L;
        private @Builder.Default String content = "댓글생성내용";

        public static TestCommentUpdateRequest.TestCommentUpdateRequestBuilder createCommentUpdateRequest() {
            return TestCommentUpdateRequest.builder();
        }

        public CommentUpdateRequest get() {
            return mapper.convertValue(this, CommentUpdateRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestCrewRequest {
        private @Builder.Default String name = "테스트크루";
        private @Builder.Default int capacity = 12;
        private @Builder.Default Region region = Region.강남구;
        private @Builder.Default String description = "크루 설명";
        private @Builder.Default int minAge = 15;
        private @Builder.Default int maxAge = 35;
        private @Builder.Default CrewGender gender = CrewGender.ANY;
        private @Builder.Default boolean permissionRequired = false;
        private @Builder.Default String question = null;
        private @Builder.Default boolean answerRequired = false;
        private @Builder.Default List<String> tags = List.of("크루태그1", "크루태그2");

        public static TestCrewRequest.TestCrewRequestBuilder createCrewRequest() {
            return TestCrewRequest.builder();
        }

        public CrewRequest get() {
            return mapper.convertValue(this, CrewRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestMeetingCreateRequest {
        private @Builder.Default LocalDateTime startTime = LocalDateTime.of(2222, 3, 14, 18, 32);
        private @Builder.Default int capacity = 5;
        private @Builder.Default Long gymId = 273L;
        private @Builder.Default Long crewId = 3153L;

        public static TestMeetingCreateRequest.TestMeetingCreateRequestBuilder createMeetingCreateRequest() {
            return TestMeetingCreateRequest.builder();
        }

        public MeetingCreateRequest get() {
            return mapper.convertValue(this, MeetingCreateRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestMeetingUpdateRequest {
        private @Builder.Default Long meetingId = 1597L;
        private @Builder.Default LocalDateTime startTime = LocalDateTime.of(2222, 3, 24, 18, 32);
        private @Builder.Default int capacity = 18;
        private @Builder.Default Long gymId = 4972L;

        public static TestMeetingUpdateRequest.TestMeetingUpdateRequestBuilder createMeetingUpdateRequest() {
            return TestMeetingUpdateRequest.builder();
        }

        public MeetingUpdateRequest get() {
            return mapper.convertValue(this, MeetingUpdateRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestReviewCreateRequest {
        private @Builder.Default String content = "여기 암장 좀 괜찮네요";
        private @Builder.Default float score = 5.0f;
        private @Builder.Default Long gymId = 1L;

        public static TestReviewCreateRequest.TestReviewCreateRequestBuilder createReviewCreateRequest() {
            return TestReviewCreateRequest.builder();
        }

        public ReviewCreateRequest get() {
            return mapper.convertValue(this, ReviewCreateRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestReviewReactionRequest {
        private @Builder.Default int reactionType = 1;

        public static TestReviewReactionRequest.TestReviewReactionRequestBuilder createReviewReactionRequest() {
            return TestReviewReactionRequest.builder();
        }

        public ReviewReactionRequest get() {
            return mapper.convertValue(this, ReviewReactionRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestReviewUpdateRequest {
        private @Builder.Default String content = "여기 암장 좀 별로네요";
        private @Builder.Default float score = 4.0f;

        public static TestReviewUpdateRequest.TestReviewUpdateRequestBuilder createReviewUpdateRequest() {
            return TestReviewUpdateRequest.builder();
        }

        public ReviewUpdateRequest get() {
            return mapper.convertValue(this, ReviewUpdateRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestUserInfoRequest {
        private @Builder.Default String nickname = "testNickname";

        public static TestUserInfoRequest.TestUserInfoRequestBuilder createUserInfoRequest() {
            return TestUserInfoRequest.builder();
        }

        public UserInfoRequest get() {
            return mapper.convertValue(this, UserInfoRequest.class);
        }
    }

    @Getter
    @Builder
    public static class TestLoginRequest {
        private @Builder.Default String code = "testOAuthAuthenticationCode";
        private @Builder.Default int signUpType = 1;

        public static TestLoginRequest.TestLoginRequestBuilder createLoginRequest() {
            return TestLoginRequest.builder();
        }

        public LoginRequest get() {
            return mapper.convertValue(this, LoginRequest.class);
        }
    }
}
