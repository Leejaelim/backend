package matchuri.backend.groupdecision.service;

import lombok.NonNull;
import matchuri.backend.groupdecision.command.CreateGroupCommand;
import matchuri.backend.groupdecision.command.DeleteGroupCommand;
import matchuri.backend.groupdecision.command.GetMyGroupsCommand;
import matchuri.backend.groupdecision.command.LeaveGroupCommand;
import matchuri.backend.groupdecision.command.UpdateGroupCommand;
import matchuri.backend.groupdecision.result.CreateGroupResult;
import matchuri.backend.groupdecision.result.DeleteGroupResult;
import matchuri.backend.groupdecision.result.GroupDetailResult;
import matchuri.backend.groupdecision.result.GroupSummaryResult;
import matchuri.backend.groupdecision.result.LeaveGroupResult;
import matchuri.backend.groupdecision.result.UpdateGroupResult;
import org.springframework.data.domain.Page;

public interface GroupManagementService {

    CreateGroupResult createGroup(Long memberId, CreateGroupCommand command);

    LeaveGroupResult leaveGroup(Long memberId, LeaveGroupCommand command);

    DeleteGroupResult deleteGroup(Long memberId, DeleteGroupCommand command);

    UpdateGroupResult updateGroup(Long memberId, UpdateGroupCommand command);

    Page<@NonNull GroupSummaryResult> getMyGroups(Long memberId, GetMyGroupsCommand command);

    GroupDetailResult getGroup(Long memberId, Long groupId);

    GroupDetailResult getGroupV2(Long memberId, Long groupId);
}

