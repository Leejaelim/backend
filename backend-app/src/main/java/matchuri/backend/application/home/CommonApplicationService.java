package matchuri.backend.application.home;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.application.api.common.dto.response.HomeResponse;
import matchuri.backend.application.api.common.mapper.HomeMapper;
import matchuri.backend.groupdecision.result.GroupHomeActivityResult;
import matchuri.backend.groupdecision.service.GroupRecommendationService;
import matchuri.backend.identity.member.result.MemberHomeResult;
import matchuri.backend.identity.member.api.MemberService;
import matchuri.backend.recommendation.result.PersonalRecommendationHomeResult;
import matchuri.backend.recommendation.api.RecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonApplicationService {

    private final MemberService memberService;
    private final RecommendationService recommendationService;
    private final GroupRecommendationService groupRecommendationService;
    private final HomeMapper homeMapper;

    @Transactional
    public HomeResponse getHome(Long memberId) {
        MemberHomeResult member = memberService.getHomeMember(memberId);
        PersonalRecommendationHomeResult recommendations = recommendationService.getHomeRecommendations(memberId);
        List<GroupHomeActivityResult> activities = groupRecommendationService.getHomeActivities(memberId);
        return homeMapper.toResponse(
                member.profile(),
                member.location(),
                member.tasteProfile(),
                recommendations,
                activities
        );
    }
}
