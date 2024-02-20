package org.oruryclient.review.controller;

import org.oruryclient.config.ControllerTest;
import org.oruryclient.config.WithUserPrincipal;
import org.oruryclient.review.converter.message.ReviewMessage;
import org.oruryclient.review.converter.request.ReviewReactionRequest;
import org.orurycommon.error.code.ReviewReactionErrorCode;
import org.orurycommon.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Controller] 리뷰 반응 관련 테스트")
@WithUserPrincipal
class ReviewReactionControllerTest extends ControllerTest {

    @DisplayName("[POST] requestbody로 반응 정보를 받아 반응을 처리한다. - 성공")
    @Test
    void given_UserIdAndReviewIdAndType_When_CreateReviewReaction_Then_Successfully() throws Exception {

        //given
        ReviewReactionRequest request = createReviewReactionRequest();
        ReviewMessage code = ReviewMessage.REVIEW_REACTION_PROCESSED;

        //when & then
        mvc.perform(post("/api/v1/reviews/reaction")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(reviewReactionService).should(times(1))
                .processReviewReaction(any());
    }

    @DisplayName("[POST] 유효하지 않은 요청(존재하지 않는 리뷰, 잘못된 반응 타입)일 경우 BAD request 예외 발생 - 실패")
    @Test
    void given_NotValidatedReviewIdOrType_When_CreateReviewReaction_Then_BadRequestException() throws Exception {

        //given
        ReviewReactionRequest request = createReviewReactionRequest();
        ReviewReactionErrorCode code = ReviewReactionErrorCode.BAD_REQUEST;

        willThrow(new BusinessException(code)).given(reviewReactionService)
                .processReviewReaction(any());

        //when & then
        mvc.perform(post("/api/v1/reviews/reaction")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(reviewReactionService).should(times(1))
                .processReviewReaction(any());
    }

    private ReviewReactionRequest createReviewReactionRequest() {
        return ReviewReactionRequest.of(
                1L,
                1
        );
    }


}
