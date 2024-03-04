package org.orury.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.auth.controller.AuthController;
import org.orury.client.auth.jwt.JwtTokenProvider;
import org.orury.client.auth.service.AuthService;
import org.orury.client.auth.strategy.LoginStrategyManager;
import org.orury.client.comment.application.CommentService;
import org.orury.client.comment.interfaces.CommentController;
import org.orury.client.gym.application.GymService;
import org.orury.client.gym.interfaces.GymController;
import org.orury.client.post.application.PostService;
import org.orury.client.post.interfaces.PostController;
import org.orury.client.review.application.ReviewService;
import org.orury.client.review.interfaces.ReviewController;
import org.orury.client.user.interfaces.UserController;
import org.orury.common.config.SlackMessage;
import org.orury.domain.post.domain.PostLikeService;
import org.orury.domain.user.domain.UserService;
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
        GymController.class,
        PostController.class,
        ReviewController.class,
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
    protected GymService gymService;

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
    protected UserService userService;

    @MockBean
    protected SlackMessage slackMessage;
}
