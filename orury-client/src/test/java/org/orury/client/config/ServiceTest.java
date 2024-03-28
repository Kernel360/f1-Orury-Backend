package org.orury.client.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.auth.application.AuthService;
import org.orury.client.auth.application.AuthServiceImpl;
import org.orury.client.auth.application.jwt.JwtTokenService;
import org.orury.client.auth.application.oauth.KakaoOAuthService;
import org.orury.client.auth.application.oauth.OAuthService;
import org.orury.client.auth.application.oauth.OAuthServiceManager;
import org.orury.client.auth.application.oauth.kakaofeign.KakaoAuthClient;
import org.orury.client.auth.application.oauth.kakaofeign.KakaoKapiClient;
import org.orury.client.comment.application.CommentService;
import org.orury.client.comment.application.CommentServiceImpl;
import org.orury.client.crew.application.CrewService;
import org.orury.client.crew.application.CrewServiceImpl;
import org.orury.client.crew.application.policy.CrewApplicationPolicy;
import org.orury.client.crew.application.policy.CrewCreatePolicy;
import org.orury.client.crew.application.policy.CrewPolicy;
import org.orury.client.crew.application.policy.CrewUpdatePolicy;
import org.orury.client.gym.application.GymService;
import org.orury.client.gym.application.GymServiceImpl;
import org.orury.client.meeting.application.MeetingService;
import org.orury.client.meeting.application.MeetingServiceImpl;
import org.orury.client.post.application.PostService;
import org.orury.client.post.application.PostServiceImpl;
import org.orury.client.review.application.ReviewService;
import org.orury.client.review.application.ReviewServiceImpl;
import org.orury.client.user.application.UserService;
import org.orury.client.user.application.UserServiceImpl;
import org.orury.domain.auth.domain.RefreshTokenReader;
import org.orury.domain.auth.domain.RefreshTokenStore;
import org.orury.domain.comment.domain.CommentReader;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.comment.infrastructure.CommentLikeRepository;
import org.orury.domain.crew.domain.*;
import org.orury.domain.gym.domain.GymReader;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.image.domain.ImageReader;
import org.orury.domain.image.domain.ImageStore;
import org.orury.domain.meeting.domain.MeetingMemberReader;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.orury.domain.meeting.domain.MeetingReader;
import org.orury.domain.meeting.domain.MeetingStore;
import org.orury.domain.post.domain.PostReader;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.post.infrastructure.PostLikeRepository;
import org.orury.domain.review.domain.ReviewReader;
import org.orury.domain.review.domain.ReviewStore;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public abstract class ServiceTest {
    protected ImageReader imageReader;
    protected ImageStore imageStore;
    protected PostReader postReader;
    protected PostStore postStore;
    protected PostLikeRepository postLikeRepository;
    protected CommentReader commentReader;
    protected CommentStore commentStore;
    protected CommentLikeRepository commentLikeRepository;
    protected RefreshTokenReader refreshTokenReader;
    protected RefreshTokenStore refreshTokenStore;
    protected UserReader userReader;
    protected UserStore userStore;
    protected AuthService authService;
    protected PostService postService;
    protected GymReader gymReader;
    protected GymStore gymStore;
    protected GymService gymService;
    protected JwtTokenService jwtTokenService;
    protected OAuthServiceManager oAuthServiceManager;
    protected OAuthService oAuthService;
    protected CommentService commentService;
    protected KakaoAuthClient kakaoAuthClient;
    protected KakaoKapiClient kakaoKapiClient;
    protected ReviewReader reviewReader;
    protected ReviewStore reviewStore;
    protected ReviewService reviewService;
    protected UserService userService;
    protected CrewReader crewReader;
    protected CrewStore crewStore;
    protected CrewTagReader crewTagReader;
    protected CrewTagStore crewTagStore;
    protected CrewMemberReader crewMemberReader;
    protected CrewMemberStore crewMemberStore;
    protected CrewService crewService;
    protected CrewApplicationReader crewApplicationReader;
    protected CrewApplicationStore crewApplicationStore;
    protected CrewPolicy crewPolicy;
    protected CrewCreatePolicy crewCreatePolicy;
    protected CrewUpdatePolicy crewUpdatePolicy;
    protected CrewApplicationPolicy crewApplicationPolicy;
    protected MeetingStore meetingStore;
    protected MeetingMemberStore meetingMemberStore;
    protected MeetingReader meetingReader;
    protected MeetingMemberReader meetingMemberReader;
    protected MeetingService meetingService;

    protected static final int KAKAO_SIGN_UP_TYPE = 1;

    /**
     * 요기에 ServiceTest를 상속받는 모든 테스트 클래스에서 일괄 적용
     */
    @BeforeEach
    void setUp() {
        //image
        imageReader = mock(ImageReader.class);
        imageStore = mock(ImageStore.class);

        //post
        postReader = mock(PostReader.class);
        postStore = mock(PostStore.class);
        postLikeRepository = mock(PostLikeRepository.class);

        //comment
        commentReader = mock(CommentReader.class);
        commentStore = mock(CommentStore.class);
        commentLikeRepository = mock(CommentLikeRepository.class);

        //crew
        crewReader = mock(CrewReader.class);
        crewStore = mock(CrewStore.class);
        crewTagReader = mock(CrewTagReader.class);
        crewTagStore = mock(CrewTagStore.class);
        crewMemberReader = mock(CrewMemberReader.class);
        crewMemberStore = mock(CrewMemberStore.class);
        crewApplicationReader = mock(CrewApplicationReader.class);
        crewApplicationStore = mock(CrewApplicationStore.class);
        crewPolicy = mock(CrewPolicy.class);
        crewCreatePolicy = mock(CrewCreatePolicy.class);
        crewUpdatePolicy = mock(CrewUpdatePolicy.class);
        crewApplicationPolicy = mock(CrewApplicationPolicy.class);

        //meeting
        meetingStore = mock(MeetingStore.class);
        meetingMemberStore = mock(MeetingMemberStore.class);
        meetingReader = mock(MeetingReader.class);
        meetingMemberReader = mock(MeetingMemberReader.class);

        //auth
        refreshTokenReader = mock(RefreshTokenReader.class);
        refreshTokenStore = mock(RefreshTokenStore.class);
        jwtTokenService = mock(JwtTokenService.class);
        oAuthServiceManager = mock(OAuthServiceManager.class);
        kakaoAuthClient = mock(KakaoAuthClient.class);
        kakaoKapiClient = mock(KakaoKapiClient.class);

        //user
        userReader = mock(UserReader.class);
        userStore = mock(UserStore.class);

        //gym
        gymReader = mock(GymReader.class);
        gymStore = mock(GymStore.class);

        //review
        reviewReader = mock(ReviewReader.class);
        reviewStore = mock(ReviewStore.class);

        //services
        authService = new AuthServiceImpl(userReader, userStore, jwtTokenService, oAuthServiceManager);
        commentService = new CommentServiceImpl(commentReader, commentStore);
        postService = new PostServiceImpl(postReader, postStore, imageStore);
        crewService = new CrewServiceImpl(crewReader, crewStore, crewTagReader, crewTagStore, crewMemberReader, crewMemberStore, crewApplicationStore, meetingStore, meetingMemberStore, userReader, imageStore, crewPolicy, crewCreatePolicy, crewUpdatePolicy, crewApplicationPolicy);
        oAuthService = new KakaoOAuthService(kakaoAuthClient, kakaoKapiClient);
        gymService = new GymServiceImpl(gymReader, gymStore);
        reviewService = new ReviewServiceImpl(reviewReader, reviewStore, gymStore, imageStore);
        userService = new UserServiceImpl(userReader, userStore, imageStore, postStore, commentStore, reviewStore, gymStore);
        meetingService = new MeetingServiceImpl(meetingReader, meetingStore, meetingMemberReader, meetingMemberStore, crewMemberReader, userReader);
    }
}