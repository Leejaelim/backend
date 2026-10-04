package matchuri.backend.application.api.group;

import lombok.RequiredArgsConstructor;
import matchuri.backend.application.api.group.dto.response.GroupInviteExistsResponse;
import matchuri.backend.groupdecision.service.GroupInviteService;
import matchuri.backend.shared.api.ApiResponse;
import matchuri.backend.identity.security.AuthenticatedMemberId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class GroupInviteController implements GroupInviteApi {

    private final GroupInviteService groupInviteService;

    @Override
    @GetMapping("/me/exists")
    public ApiResponse<GroupInviteExistsResponse> checkMyInviteExists(
            @AuthenticatedMemberId Long memberId
    ) {
        return ApiResponse.success(new GroupInviteExistsResponse(
                groupInviteService.existsMyPendingInvite(memberId)
        ));
    }
}
