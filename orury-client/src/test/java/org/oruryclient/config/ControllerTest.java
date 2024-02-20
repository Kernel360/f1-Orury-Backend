package org.oruryclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.oruryclient.auth.controller.AuthController;
import org.oruryclient.auth.jwt.JwtTokenProvider;
import org.oruryclient.auth.service.AuthService;
import org.oruryclient.auth.strategy.LoginStrategyManager;
import org.oruryclient.comment.controller.CommentController;
import org.oruryclient.comment.controller.CommentLikeController;
import org.oruryclient.comment.service.CommentLikeService;
import org.oruryclient.comment.service.CommentService;
import org.oruryclient.gym.controller.GymController;
import org.oruryclient.gym.controller.GymLikeController;
import org.oruryclient.gym.service.GymLikeService;
import org.oruryclient.gym.service.GymService;
import org.oruryclient.post.controller.PostController;
import org.oruryclient.post.controller.PostLikeController;
import org.oruryclient.post.service.PostLikeService;
import org.oruryclient.post.service.PostService;
import org.oruryclient.review.controller.ReviewController;
import org.oruryclient.review.controller.ReviewReactionController;
import org.oruryclient.review.service.ReviewReactionService;
import org.oruryclient.review.service.ReviewService;
import org.oruryclient.user.controller.UserController;
import org.oruryclient.user.service.UserService;
import org.orurycommon.config.SlackMessage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@WebMvcTest({
        AuthController.class,
        CommentController.class,
        CommentLikeController.class,
        GymController.class,
        GymLikeController.class,
        PostController.class,
        PostLikeController.class,
        ReviewController.class,
        ReviewReactionController.class,
        UserController.class
})
@ActiveProfiles("test")
public abstract class ControllerTest {
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected CommentLikeService commentLikeService;

    @MockBean
    protected GymService gymService;

    @MockBean
    protected GymLikeService gymLikeService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected LoginStrategyManager loginStrategyManager;

    @MockBean
    protected PostService postService;

    @MockBean
    protected PostLikeService postLikeService;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected ReviewReactionService reviewReactionService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected SlackMessage slackMessage;
}
