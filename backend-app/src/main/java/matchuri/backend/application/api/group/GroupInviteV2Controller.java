package matchuri.backend.application.api.group;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import matchuri.backend.application.api.group.dto.response.GroupInviteV2SummaryResponse;
import matchuri.backend.application.api.group.mapper.GroupMapper;
import matchuri.backend.groupdecision.command.GetMyGroupInvitesCommand;
import matchuri.backend.groupdecision.entity.GroupInviteStatus;
import matchuri.backend.groupdecision.result.GroupInviteV2SummaryResult;
import matchuri.backend.groupdecision.service.GroupInviteService;
import matchuri.backend.shared.api.ApiResponse;
import matchuri.backend.shared.api.PageResponse;
import matchuri.backend.identity.security.AuthenticatedMemberId;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/invites")
@Validated
@RequiredArgsConstructor
public class GroupInviteV2Controller implements GroupInviteV2Api {

    private final GroupInviteService groupInviteService;
    private final GroupMapper groupMapper;

    @Override
    @GetMapping("/me")
    public ApiResponse<PageResponse<GroupInviteV2SummaryResponse>> getMyInvites(
            @AuthenticatedMemberId Long memberId,
            @RequestParam(required = false) GroupInviteStatus status,
            @Min(0) @RequestParam(defaultValue = "0") Integer page,
            @Min(1) @Max(100) @RequestParam(defaultValue = "20") Integer size
    ) {
        GetMyGroupInvitesCommand command = groupMapper.toGetMyGroupInvitesCommand(status, page, size);
        Page<@NonNull GroupInviteV2SummaryResult> results = groupInviteService.getMyInvitesV2(memberId, command);

        return ApiResponse.success(PageResponse.of(results, groupMapper::toGroupInviteV2SummaryResponse));
    }
}
