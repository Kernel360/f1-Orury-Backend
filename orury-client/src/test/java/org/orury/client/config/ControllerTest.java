package org.orury.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.client.auth.application.AuthFacade;
import org.orury.client.auth.interfaces.AuthController;
import org.orury.client.comment.application.CommentFacade;
import org.orury.client.comment.interfaces.CommentController;
import org.orury.client.crew.application.CrewFacade;
import org.orury.client.crew.interfaces.CrewController;
import org.orury.client.gym.application.GymFacade;
import org.orury.client.gym.interfaces.GymController;
import org.orury.client.meeting.application.MeetingFacade;
import org.orury.client.meeting.interfaces.MeetingController;
import org.orury.client.notification.application.NotificationFacade;
import org.orury.client.notification.interfaces.NotificationController;
import org.orury.client.post.application.PostFacade;
import org.orury.client.post.interfaces.PostController;
import org.orury.client.review.application.ReviewFacade;
import org.orury.client.review.interfaces.ReviewController;
import org.orury.client.user.application.UserFacade;
import org.orury.client.user.interfaces.UserController;
import org.orury.common.config.SlackMessage;
import org.orury.domain.notification.infrastructure.EmitterRepository;
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
        CrewController.class,
        GymController.class,
        MeetingController.class,
        NotificationController.class,
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
    protected AuthFacade authFacade;

    @MockBean
    protected CommentFacade commentFacade;

    @MockBean
    protected CrewFacade crewFacade;

    @MockBean
    protected GymFacade gymFacade;

    @MockBean
    protected MeetingFacade meetingFacade;

    @MockBean
    protected NotificationFacade notificationFacade;

    @MockBean
    protected PostFacade postFacade;

    @MockBean
    protected ReviewFacade reviewFacade;

    @MockBean
    protected UserFacade userFacade;

    @MockBean
    protected SlackMessage slackMessage;

    @MockBean
    protected EmitterRepository emitterRepository;
}
