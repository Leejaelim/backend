package matchuri.backend.domain.common.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.api.common.dto.response.HomeResponse;
import matchuri.backend.api.common.mapper.HomeMapper;
import matchuri.backend.domain.group.result.GroupHomeActivityResult;
import matchuri.backend.domain.group.service.GroupService;
import matchuri.backend.domain.member.result.MemberLocationResult;
import matchuri.backend.domain.member.result.MemberProfileResult;
import matchuri.backend.domain.member.result.MemberTasteProfileSummaryResult;
import matchuri.backend.domain.member.service.MemberService;
import matchuri.backend.domain.recommendation.result.PersonalRecommendationHomeResult;
import matchuri.backend.domain.recommendation.service.RecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonApplicationService {

    private final MemberService memberService;
    private final RecommendationService recommendationService;
    private final GroupService groupService;
    private final HomeMapper homeMapper;

    @Transactional
    public HomeResponse getHome(Long memberId) {
        MemberProfileResult user = memberService.getMyProfile(memberId);
        MemberLocationResult location = memberService.getMyLocation(memberId);
        MemberTasteProfileSummaryResult taste = memberService.getMyTasteProfile(memberId);
        PersonalRecommendationHomeResult recommendations = recommendationService.getHomeRecommendations(memberId);
        List<GroupHomeActivityResult> activities = groupService.getHomeActivities(memberId);
        return homeMapper.toResponse(user, location, taste, recommendations, activities);
    }
}
