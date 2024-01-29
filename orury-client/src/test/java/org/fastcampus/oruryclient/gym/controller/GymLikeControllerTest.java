package org.fastcampus.oruryclient.gym.controller;

import org.fastcampus.oruryclient.config.ControllerTest;
import org.fastcampus.oruryclient.config.WithUserPrincipal;
import org.fastcampus.oruryclient.gym.converter.message.GymMessage;
import org.fastcampus.orurycommon.error.code.GymErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[Controller] 암장 좋아요 관련 테스트")
@WithUserPrincipal
class GymLikeControllerTest extends ControllerTest {

    @DisplayName("[POST] 암장 id로 암장 좋아요 생성 - 성공")
    @Test
    void when_CreateExistingGymId_Then_CreateGymLikeSuccessfully() throws Exception {
        //given
        Long gymId = 1L;

        GymMessage message = GymMessage.GYM_LIKE_CREATED;

        willDoNothing().given(gymService)
                .isValidate(anyLong());

        //when & then
        mvc.perform(post("/api/v1/gyms/like/" + gymId).accept("application/json")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        then(gymService).should()
                .isValidate(anyLong());
        then(gymLikeService).should()
                .createGymLike(any());
    }

    @DisplayName("[POST] 암장 id로 암장 좋아요 생성 - 실패 (존재하지 않는 암장 id인 경우)")
    @Test
    void when_CreateNotExistingGymId_Then_NotFoundException() throws Exception {
        //given
        Long gymId = 1L;

        GymErrorCode code = GymErrorCode.NOT_FOUND;

        willThrow(new BusinessException(code)).given(gymService)
                .isValidate(any());

        //when & then
        mvc.perform(post("/api/v1/gyms/like/" + gymId).accept("application/json")
                        .with(csrf()))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(gymService).should()
                .isValidate(anyLong());
        then(gymLikeService).should(never())
                .createGymLike(any());
    }

    @DisplayName("[DELETE] 암장 id로 암장 좋아요 삭제 - 성공")
    @Test
    void when_DeleteExistingGymId_Then_DeleteGymLikeSuccessfully() throws Exception {
        //given
        Long gymId = 1L;

        GymMessage message = GymMessage.GYM_LIKE_DELETED;

        willDoNothing().given(gymService)
                .isValidate(anyLong());

        //when & then
        mvc.perform(delete("/api/v1/gyms/like/" + gymId).accept("application/json")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message.getMessage()))
        ;

        then(gymService).should()
                .isValidate(anyLong());
        then(gymLikeService).should()
                .deleteGymLike(any());
    }

    @DisplayName("[DELETE] 암장 id로 암장 좋아요 삭제 - 실패 (존재하지 않는 암장 id인 경우)")
    @Test
    void when_DeleteNotExistingGymId_Then_NotFoundException() throws Exception {
        //given
        Long gymId = 1L;

        GymErrorCode code = GymErrorCode.NOT_FOUND;

        willThrow(new BusinessException(code)).given(gymService)
                .isValidate(any());

        //when & then
        mvc.perform(delete("/api/v1/gyms/like/" + gymId).accept("application/json")
                        .with(csrf()))
                .andExpect(status().is(code.getStatus()))
                .andExpect(jsonPath("$.message").value(code.getMessage()))
        ;

        then(gymService).should()
                .isValidate(anyLong());
        then(gymLikeService).should(never())
                .deleteGymLike(any());
    }
}
