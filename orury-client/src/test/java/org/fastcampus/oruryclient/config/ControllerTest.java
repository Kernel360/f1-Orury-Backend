package org.fastcampus.oruryclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fastcampus.oruryclient.auth.controller.AuthController;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.oruryclient.auth.service.AuthService;
import org.fastcampus.oruryclient.auth.strategy.LoginStrategyManager;
import org.fastcampus.oruryclient.comment.controller.CommentController;
import org.fastcampus.oruryclient.comment.controller.CommentLikeController;
import org.fastcampus.oruryclient.comment.service.CommentLikeService;
import org.fastcampus.oruryclient.comment.service.CommentService;
import org.fastcampus.oruryclient.gym.controller.GymController;
import org.fastcampus.oruryclient.gym.controller.GymLikeController;
import org.fastcampus.oruryclient.gym.service.GymLikeService;
import org.fastcampus.oruryclient.gym.service.GymService;
import org.fastcampus.oruryclient.post.controller.PostController;
import org.fastcampus.oruryclient.post.controller.PostLikeController;
import org.fastcampus.oruryclient.post.service.PostLikeService;
import org.fastcampus.oruryclient.post.service.PostService;
import org.fastcampus.oruryclient.review.controller.ReviewController;
import org.fastcampus.oruryclient.review.controller.ReviewReactionController;
import org.fastcampus.oruryclient.review.service.ReviewReactionService;
import org.fastcampus.oruryclient.review.service.ReviewService;
import org.fastcampus.oruryclient.user.controller.UserController;
import org.fastcampus.oruryclient.user.service.UserService;
import org.fastcampus.orurycommon.config.SlackMessage;
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
