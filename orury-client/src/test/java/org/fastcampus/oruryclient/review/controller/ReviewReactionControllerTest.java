package org.fastcampus.oruryclient.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.oruryclient.config.WithUserPrincipal;
import org.fastcampus.oruryclient.review.converter.message.ReviewMessage;
import org.fastcampus.oruryclient.review.converter.request.ReviewReactionRequest;
import org.fastcampus.orurycommon.error.code.ReviewReactionErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Controller] 리뷰 반응 관련 테스트")
@WithUserPrincipal
public class ReviewReactionControllerTest extends ControllerTest {

    @DisplayName("[PUT] requestbody로 반응 정보를 받아 반응을 생성, 수정한다. - 성공")
    @Test
    void given_UserIdAndReviewIdAndType_When_CreateReviewReaction_Then_Successfully() throws Exception {

        //given
        ReviewReactionRequest request = createReviewReactionRequest();
        ReviewMessage code = ReviewMessage.REVIEW_REACTION_CREATED;

        //when & then
        mvc.perform(put("/review/reaction")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(reviewReactionService).should(times(1)).createReviewReaction(any());
    }

    @DisplayName("[PUT] 유효하지 않은 요청(존재하지 않는 리뷰, 잘못된 반응 타입)일 경우 BAD request 예외 발생 - 실패")
    @Test
    void given_NotValidatedReviewIdOrType_When_CreateReviewReaction_Then_BadRequestException() throws Exception {

        //given
        ReviewReactionRequest request = createReviewReactionRequest();
        ReviewReactionErrorCode code = ReviewReactionErrorCode.BAD_REQUEST;

        willThrow(new BusinessException(code)).given(reviewReactionService).createReviewReaction(any());

        //when & then
        mvc.perform(put("/review/reaction")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(reviewReactionService).should(times(1)).createReviewReaction(any());
    }

    @DisplayName("[DELETE] reviewId, UserId로 생성된 반응이 있으면(=삭제할 반응이 있다면), 반응을 삭제한다. - 성공")
    @Test
    void given_ValidatedUserIdAndReviewId_When_DeleteReviewReaction_Then_Successfully() throws Exception {
        //given
        ReviewMessage code = ReviewMessage.REVIEW_REACTION_DELETED;

        //when & then
        mvc.perform(delete("/review/reaction/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(code.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
        ;

        then(reviewReactionService).should(times(1)).deleteReviewReaction(any());
    }

    @DisplayName("[DELETE] reviewId, UserId로 생성된 반응이 없으면(=삭제할 반응이 없으면), 리뷰 반응 삭제 예외 처리 발생 - 실패")
    @Test
    void given_NotValidatedUserIdAndReviewId_When_DeleteReviewReaction_Then_NotFoundException() throws Exception {
        //given
        ReviewReactionErrorCode code = ReviewReactionErrorCode.NOT_FOUND;

        willThrow(new BusinessException(code)).given(reviewReactionService).deleteReviewReaction(any());

        //when & then
        mvc.perform(delete("/review/reaction/" + 1L)
                        .with(csrf())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(reviewReactionService).should(times(1)).deleteReviewReaction(any());
    }


    private ReviewReactionRequest createReviewReactionRequest() {
        return ReviewReactionRequest.of(
                1L,
                1
        );
    }


}
