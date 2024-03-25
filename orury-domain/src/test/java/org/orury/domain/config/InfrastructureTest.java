package org.orury.domain.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.admin.domain.AdminReader;
import org.orury.domain.admin.domain.AdminReaderImpl;
import org.orury.domain.admin.domain.AdminStore;
import org.orury.domain.admin.domain.AdminStoreImpl;
import org.orury.domain.admin.infrastructure.AdminRepository;
import org.orury.domain.auth.domain.RefreshTokenReader;
import org.orury.domain.auth.domain.RefreshTokenStore;
import org.orury.domain.auth.infrastructure.RefreshTokenReaderImpl;
import org.orury.domain.auth.infrastructure.RefreshTokenRepository;
import org.orury.domain.auth.infrastructure.RefreshTokenStoreImpl;
import org.orury.domain.comment.domain.CommentReader;
import org.orury.domain.comment.domain.CommentStore;
import org.orury.domain.comment.infrastructure.CommentLikeRepository;
import org.orury.domain.comment.infrastructure.CommentReaderImpl;
import org.orury.domain.comment.infrastructure.CommentRepository;
import org.orury.domain.comment.infrastructure.CommentStoreImpl;
import org.orury.domain.crew.domain.*;
import org.orury.domain.crew.infrastructures.*;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.global.image.ImageStore;
import org.orury.domain.gym.domain.GymReader;
import org.orury.domain.gym.domain.GymStore;
import org.orury.domain.gym.infrastructure.GymLikeRepository;
import org.orury.domain.gym.infrastructure.GymReaderImpl;
import org.orury.domain.gym.infrastructure.GymRepository;
import org.orury.domain.gym.infrastructure.GymStoreImpl;
import org.orury.domain.meeting.domain.MeetingMemberReader;
import org.orury.domain.meeting.domain.MeetingMemberStore;
import org.orury.domain.meeting.domain.MeetingReader;
import org.orury.domain.meeting.domain.MeetingStore;
import org.orury.domain.meeting.infrastructure.*;
import org.orury.domain.notice.domain.NoticeReader;
import org.orury.domain.notice.domain.NoticeStore;
import org.orury.domain.notice.infrastructure.NoticeReaderImpl;
import org.orury.domain.notice.infrastructure.NoticeRepository;
import org.orury.domain.notice.infrastructure.NoticeStoreImpl;
import org.orury.domain.post.domain.PostReader;
import org.orury.domain.post.domain.PostStore;
import org.orury.domain.post.infrastructure.PostLikeRepository;
import org.orury.domain.post.infrastructure.PostReaderImpl;
import org.orury.domain.post.infrastructure.PostRepository;
import org.orury.domain.post.infrastructure.PostStoreImpl;
import org.orury.domain.review.domain.ReviewReader;
import org.orury.domain.review.domain.ReviewStore;
import org.orury.domain.review.infrastructure.ReviewReactionRepository;
import org.orury.domain.review.infrastructure.ReviewReaderImpl;
import org.orury.domain.review.infrastructure.ReviewRepository;
import org.orury.domain.review.infrastructure.ReviewStoreImpl;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.infrastucture.UserReaderImpl;
import org.orury.domain.user.infrastucture.UserRepository;
import org.orury.domain.user.infrastucture.UserStoreImpl;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public abstract class InfrastructureTest {
    //admin
    protected AdminRepository adminRepository;
    protected AdminReader adminReader;
    protected AdminStore adminStore;
    //auth
    protected RefreshTokenRepository refreshTokenRepository;
    protected RefreshTokenReader refreshTokenReader;
    protected RefreshTokenStore refreshTokenStore;
    //comment
    protected CommentReader commentReader;
    protected CommentStore commentStore;
    protected CommentRepository commentRepository;
    protected CommentLikeRepository commentLikeRepository;
    //crew
    protected CrewApplicationReader crewApplicationReader;
    protected CrewApplicationStore crewApplicationStore;
    protected CrewMemberReader crewMemberReader;
    protected CrewMemberStore crewMemberStore;
    protected CrewReader crewReader;
    protected CrewStore crewStore;
    protected CrewTagReader crewTagReader;
    protected CrewTagStore crewTagStore;
    protected CrewApplicationRepository crewApplicationRepository;
    protected CrewMemberRepository crewMemberRepository;
    protected CrewRepository crewRepository;
    protected CrewTagRepository crewTagRepository;
    //gym
    protected GymReader gymReader;
    protected GymStore gymStore;
    protected GymRepository gymRepository;
    protected GymLikeRepository gymLikeRepository;
    //meeting
    protected MeetingReader meetingReader;
    protected MeetingStore meetingStore;
    protected MeetingRepository meetingRepository;
    protected MeetingMemberRepository meetingMemberRepository;
    protected MeetingMemberReader meetingMemberReader;
    protected MeetingMemberStore meetingMemberStore;
    //notice
    protected NoticeRepository noticeRepository;
    protected NoticeReader noticeReader;
    protected NoticeStore noticeStore;
    //post
    protected PostRepository postRepository;
    protected PostLikeRepository postLikeRepository;
    protected PostReader postReader;
    protected PostStore postStore;
    //image
    protected ImageReader imageReader;
    protected ImageStore imageStore;
    // review
    protected ReviewReader reviewReader;
    protected ReviewStore reviewStore;
    protected ReviewRepository reviewRepository;
    protected ReviewReactionRepository reviewReactionRepository;
    //user
    protected UserRepository userRepository;
    protected UserReader userReader;
    protected UserStore userStore;

    @BeforeEach
    void setUp() {
        //admin
        adminRepository = mock(AdminRepository.class);

        //auth
        refreshTokenRepository = mock(RefreshTokenRepository.class);

        //post
        postRepository = mock(PostRepository.class);
        postLikeRepository = mock(PostLikeRepository.class);

        //comment
        commentRepository = mock(CommentRepository.class);
        commentLikeRepository = mock(CommentLikeRepository.class);

        //crew
        crewRepository = mock(CrewRepository.class);
        crewApplicationRepository = mock(CrewApplicationRepository.class);
        crewMemberRepository = mock(CrewMemberRepository.class);
        crewTagRepository = mock(CrewTagRepository.class);

        //meeting
        meetingRepository = mock(MeetingRepository.class);
        meetingMemberRepository = mock(MeetingMemberRepository.class);

        //notice
        noticeRepository = mock(NoticeRepository.class);

        //user
        userRepository = mock(UserRepository.class);

        //gym
        gymRepository = mock(GymRepository.class);
        gymLikeRepository = mock(GymLikeRepository.class);

        //review
        reviewRepository = mock(ReviewRepository.class);
        reviewReactionRepository = mock(ReviewReactionRepository.class);

        //image
        imageReader = mock(ImageReader.class);
        imageStore = mock(ImageStore.class);

        //admin
        adminReader = new AdminReaderImpl(adminRepository);
        adminStore = new AdminStoreImpl(adminRepository);
        //auth
        refreshTokenReader = new RefreshTokenReaderImpl(refreshTokenRepository);
        refreshTokenStore = new RefreshTokenStoreImpl(refreshTokenRepository);
        //comment
        commentReader = new CommentReaderImpl(commentRepository, commentLikeRepository);
        commentStore = new CommentStoreImpl(commentRepository, commentLikeRepository, postRepository);
        //crew
        crewApplicationReader = new CrewApplicationReaderImpl(crewApplicationRepository);
        crewApplicationStore = new CrewApplicationStoreImpl(crewApplicationRepository, crewRepository, crewMemberRepository);
        crewMemberReader = new CrewMemberReaderImpl(crewMemberRepository, userRepository);
        crewMemberStore = new CrewMemberStoreImpl(crewMemberRepository, crewRepository);
        crewReader = new CrewReaderImpl(crewRepository, crewMemberRepository);
        crewStore = new CrewStoreImpl(crewRepository);
        crewTagReader = new CrewTagReaderImpl(crewTagRepository);
        crewTagStore = new CrewTagStoreImpl(crewTagRepository);
        //gym
        gymReader = new GymReaderImpl(gymRepository, gymLikeRepository);
        gymStore = new GymStoreImpl(gymRepository, gymLikeRepository);
        //meeting
        meetingReader = new MeetingReaderImpl(meetingRepository);
        meetingStore = new MeetingStoreImpl(meetingRepository);
        meetingMemberReader = new MeetingMemberReaderImpl(meetingMemberRepository);
        meetingMemberStore = new MeetingMemberStoreImpl(meetingMemberRepository, meetingRepository);
        //notice
        noticeReader = new NoticeReaderImpl(noticeRepository);
        noticeStore = new NoticeStoreImpl(noticeRepository);
        //post
        postReader = new PostReaderImpl(postRepository, postLikeRepository);
        postStore = new PostStoreImpl(postRepository, postLikeRepository, imageStore);
        //review
        reviewReader = new ReviewReaderImpl(reviewRepository, reviewReactionRepository);
        reviewStore = new ReviewStoreImpl(reviewRepository, reviewReactionRepository);
        //user
        userReader = new UserReaderImpl(userRepository);
        userStore = new UserStoreImpl(userRepository);
    }
}
