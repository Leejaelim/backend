package matchuri.backend.domain.group.service;

import lombok.NonNull;
import java.util.List;
import matchuri.backend.domain.group.result.GroupHomeActivityResult;
import matchuri.backend.domain.group.command.CreateGroupCommand;
import matchuri.backend.domain.group.command.CreateGroupRecommendationCommand;
import matchuri.backend.domain.group.command.CreateNicknameGroupInviteCommand;
import matchuri.backend.domain.group.command.DeleteGroupCommand;
import matchuri.backend.domain.group.command.FinalizeGroupRecommendationCommand;
import matchuri.backend.domain.group.command.GetMyGroupInvitesCommand;
import matchuri.backend.domain.group.command.GetMyGroupsCommand;
import matchuri.backend.domain.group.command.JoinGroupCommand;
import matchuri.backend.domain.group.command.LeaveGroupCommand;
import matchuri.backend.domain.group.command.RespondGroupInviteCommand;
import matchuri.backend.domain.group.command.UpdateGroupCommand;
import matchuri.backend.domain.group.entity.GroupRecommendationRerollType;
import matchuri.backend.domain.group.result.CreateGroupResult;
import matchuri.backend.domain.group.result.CreateGroupRecommendationResult;
import matchuri.backend.domain.group.result.CreateNicknameGroupInviteResult;
import matchuri.backend.domain.group.result.DeleteGroupResult;
import matchuri.backend.domain.group.result.FinalizeGroupRecommendationResult;
import matchuri.backend.domain.group.result.GroupDetailResult;
import matchuri.backend.domain.group.result.GroupInviteSummaryResult;
import matchuri.backend.domain.group.result.GroupInviteLinkResult;
import matchuri.backend.domain.group.result.GroupRecommendationCandidateListResult;
import matchuri.backend.domain.group.result.GroupRecommendationReadinessResult;
import matchuri.backend.domain.group.result.GroupRecommendationResult;
import matchuri.backend.domain.group.result.GroupRecommendationSummaryResult;
import matchuri.backend.domain.group.result.GroupSummaryResult;
import matchuri.backend.domain.group.result.GroupVoteResult;
import matchuri.backend.domain.group.result.JoinGroupResult;
import matchuri.backend.domain.group.result.LeaveGroupResult;
import matchuri.backend.domain.group.result.RespondGroupInviteResult;
import matchuri.backend.domain.group.result.ReadyGroupRecommendationResult;
import matchuri.backend.domain.group.result.UpdateGroupResult;
import org.springframework.data.domain.Page;

public interface GroupService {

    List<GroupHomeActivityResult> getHomeActivities(Long memberId);

    CreateGroupResult createGroup(Long memberId, CreateGroupCommand command);

    CreateGroupRecommendationResult createGroupRecommendation(Long memberId, CreateGroupRecommendationCommand command);

    CreateGroupRecommendationResult rerollGroupRecommendation(Long memberId, Long groupId, Long sessionId, GroupRecommendationRerollType rerollType, String contextJson);

    GroupRecommendationResult getGroupRecommendation(Long memberId, Long groupId, Long sessionId);

    GroupRecommendationCandidateListResult getGroupRecommendationCandidates(Long memberId, Long groupId, Long sessionId);

    Page<GroupRecommendationSummaryResult> getGroupRecommendations(Long memberId, Long groupId, int page, int size);

    GroupRecommendationReadinessResult getGroupRecommendationReadiness(Long memberId, Long groupId, Long sessionId);

    ReadyGroupRecommendationResult readyGroupRecommendation(Long memberId, Long groupId, Long sessionId);

    GroupVoteResult voteGroupRecommendation(Long memberId, Long groupId, Long sessionId, Long candidateId);

    FinalizeGroupRecommendationResult finalizeGroupRecommendation(Long memberId, FinalizeGroupRecommendationCommand command);

    CreateNicknameGroupInviteResult createNicknameInvite(Long memberId, CreateNicknameGroupInviteCommand command);

    GroupInviteLinkResult createInviteLink(Long memberId, Long groupId);

    GroupInviteLinkResult reissueInviteLink(Long memberId, Long groupId);

    GroupInviteLinkResult getCurrentInviteLink(Long memberId, Long groupId);

    JoinGroupResult joinGroupByInviteLink(Long memberId, String token);

    JoinGroupResult joinGroup(Long memberId, JoinGroupCommand command);

    LeaveGroupResult leaveGroup(Long memberId, LeaveGroupCommand command);

    DeleteGroupResult deleteGroup(Long memberId, DeleteGroupCommand command);

    UpdateGroupResult updateGroup(Long memberId, UpdateGroupCommand command);

    Page<@NonNull GroupSummaryResult> getMyGroups(Long memberId, GetMyGroupsCommand command);

    Page<@NonNull GroupInviteSummaryResult> getMyInvites(Long memberId, GetMyGroupInvitesCommand command);

    RespondGroupInviteResult respondGroupInvite(Long memberId, RespondGroupInviteCommand command);

    GroupDetailResult getGroup(Long memberId, Long groupId);
}
