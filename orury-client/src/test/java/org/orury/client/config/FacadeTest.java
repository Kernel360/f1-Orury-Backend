package org.orury.client.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.auth.application.AuthFacade;
import org.orury.client.auth.application.AuthService;
import org.orury.client.comment.application.CommentFacade;
import org.orury.client.comment.application.CommentService;
import org.orury.client.gym.application.GymFacade;
import org.orury.client.gym.application.GymService;
import org.orury.client.post.application.PostService;
import org.orury.client.review.application.ReviewFacade;
import org.orury.client.review.application.ReviewService;
import org.orury.client.user.application.UserFacade;
import org.orury.client.user.application.UserService;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public abstract class FacadeTest {
    protected AuthFacade authFacade;
    protected AuthService authService;
    protected CommentFacade commentFacade;
    protected CommentService commentService;
    protected PostService postService;
    protected UserService userService;
    protected GymFacade gymFacade;
    protected GymService gymService;
    protected ReviewService reviewService;
    protected ReviewFacade reviewFacade;
    protected UserFacade userFacade;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        commentService = mock(CommentService.class);
        postService = mock(PostService.class);
        userService = mock(UserService.class);
        gymService = mock(GymService.class);
        reviewService = mock(ReviewService.class);
        userService = mock(UserService.class);
        gymService = mock(GymService.class);

        userFacade = new UserFacade(userService, postService, reviewService, commentService);
        gymFacade = new GymFacade(gymService, reviewService);
        commentFacade = new CommentFacade(commentService, postService, userService);
        authFacade = new AuthFacade(authService);
        reviewFacade = new ReviewFacade(reviewService, userService, gymService);
    }
}
