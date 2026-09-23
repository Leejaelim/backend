package matchuri.backend.api.group;

import lombok.RequiredArgsConstructor;
import matchuri.backend.api.group.dto.response.GroupDetailV2Response;
import matchuri.backend.api.group.mapper.GroupMapper;
import matchuri.backend.domain.group.result.GroupDetailResult;
import matchuri.backend.domain.group.service.GroupManagementService;
import matchuri.backend.global.api.ApiResponse;
import matchuri.backend.global.security.AuthenticatedMemberId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/groups")
@RequiredArgsConstructor
public class GroupV2Controller implements GroupV2Api {

    private final GroupManagementService groupManagementService;
    private final GroupMapper groupMapper;

    @Override
    @GetMapping("/{groupId}")
    public ApiResponse<GroupDetailV2Response> getGroup(
            @AuthenticatedMemberId Long memberId,
            @PathVariable Long groupId
    ) {
        GroupDetailResult result = groupManagementService.getGroupV2(memberId, groupId);

        return ApiResponse.success(groupMapper.toGroupDetailV2Response(result));
    }
}
