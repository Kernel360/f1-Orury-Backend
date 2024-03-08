package org.orury.client.crew.interfaces;

import org.orury.client.crew.application.CrewFacade;
import org.orury.client.crew.interfaces.message.CrewMessage;
import org.orury.client.crew.interfaces.response.CrewResponse;
import org.orury.client.crew.interfaces.response.CrewsResponseByMyCrew;
import org.orury.client.crew.interfaces.response.CrewsResponseByRank;
import org.orury.client.crew.interfaces.response.CrewsResponseByRecommend;
import org.orury.domain.base.converter.ApiResponse;
import org.orury.domain.user.domain.dto.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
public class CrewController {
    private final CrewFacade crewFacade;

    @Operation(summary = "크루 랭킹순 조회", description = "크루를 랭킹 순으로 조회한다.")
    @GetMapping("/rank")
    public ApiResponse getCrewsByRank(@RequestParam int page, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Page<CrewsResponseByRank> pageResponse = crewFacade.getCrewsByRank(page);

        return ApiResponse.of(CrewMessage.CREWS_READ.getMessage(), pageResponse);
    }

    @Operation(summary = "크루 추천순 조회", description = "크루를 추천 순으로 조회한다.")
    @GetMapping("/recommend")
    public ApiResponse getCrewsByRecommendation(@RequestParam int page, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Page<CrewsResponseByRecommend> pageResponse = crewFacade.getCrewsByRecommend(page);

        return ApiResponse.of(CrewMessage.CREWS_READ.getMessage(), pageResponse);
    }

    @Operation(summary = "내 크루 조회", description = "내가 가입한 크루를 조회한다.")
    @GetMapping("/mycrew")
    public ApiResponse getMyCrews(@RequestParam int page, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Page<CrewsResponseByMyCrew> pageResponse = crewFacade.getMyCrews(userPrincipal.id(), page);

        return ApiResponse.of(CrewMessage.CREWS_READ.getMessage(), pageResponse);
    }

    @Operation(summary = "크루 상세 조회", description = "크루를 상세 조회한다. 크루 가입 여부에 따라 가입o-> 크루 일정 조회/ 가입x-> 크루 소개글, 크루 가입 버튼이 뜬다." +
            "가입 여부에 따른 API 호출은 어디에서 할지 프론트와 상의해야함.")
    @GetMapping("/{crewId}")
    public ApiResponse getCrewByCrewId(@PathVariable Long crewId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        CrewResponse response = crewFacade.getCrewByCrewId(userPrincipal.id(), crewId);

        return ApiResponse.of(CrewMessage.CREW_READ.getMessage(), response);
    }


}
