package matchuri.backend.application.api.common;

import lombok.RequiredArgsConstructor;
import matchuri.backend.application.api.common.dto.response.HomeResponse;
import matchuri.backend.application.home.CommonApplicationService;
import matchuri.backend.shared.api.ApiResponse;
import matchuri.backend.identity.security.AuthenticatedMemberId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommonController implements CommonApi {

    private final CommonApplicationService commonApplicationService;

    @Override
    @GetMapping("/home")
    public ApiResponse<HomeResponse> home(@AuthenticatedMemberId Long memberId) {
        return ApiResponse.success(commonApplicationService.getHome(memberId));
    }
}
