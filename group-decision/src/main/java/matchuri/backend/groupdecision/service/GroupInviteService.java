package matchuri.backend.groupdecision.service;

import java.util.Optional;
import lombok.NonNull;
import matchuri.backend.groupdecision.command.CreateNicknameGroupInviteCommand;
import matchuri.backend.groupdecision.command.GetMyGroupInvitesCommand;
import matchuri.backend.groupdecision.command.JoinGroupCommand;
import matchuri.backend.groupdecision.command.RespondGroupInviteCommand;
import matchuri.backend.groupdecision.result.CreateNicknameGroupInviteResult;
import matchuri.backend.groupdecision.result.GroupInviteLinkResult;
import matchuri.backend.groupdecision.result.GroupInviteLinkPreviewResult;
import matchuri.backend.groupdecision.result.GroupInviteSummaryResult;
import matchuri.backend.groupdecision.result.GroupInviteV2SummaryResult;
import matchuri.backend.groupdecision.result.JoinGroupResult;
import matchuri.backend.groupdecision.result.RespondGroupInviteResult;
import org.springframework.data.domain.Page;

public interface GroupInviteService {

    CreateNicknameGroupInviteResult createNicknameInvite(Long memberId, CreateNicknameGroupInviteCommand command);

    GroupInviteLinkResult createInviteLink(Long memberId, Long groupId);

    GroupInviteLinkResult reissueInviteLink(Long memberId, Long groupId);

    Optional<GroupInviteLinkResult> getCurrentInviteLink(Long memberId, Long groupId);

    GroupInviteLinkPreviewResult previewInviteLink(String token);

    JoinGroupResult joinGroupByInviteLink(Long memberId, String token);

    JoinGroupResult joinGroup(Long memberId, JoinGroupCommand command);

    Page<@NonNull GroupInviteSummaryResult> getMyInvites(Long memberId, GetMyGroupInvitesCommand command);

    Page<@NonNull GroupInviteV2SummaryResult> getMyInvitesV2(Long memberId, GetMyGroupInvitesCommand command);

    boolean existsMyPendingInvite(Long memberId);

    RespondGroupInviteResult respondGroupInvite(Long memberId, RespondGroupInviteCommand command);
}
