package matchuri.backend.identity.member.api;

import java.util.List;
import matchuri.backend.identity.member.command.CreateMemberCommand;
import matchuri.backend.identity.member.command.PutMemberLocationCommand;
import matchuri.backend.identity.member.command.RegisterLocalMemberCommand;
import matchuri.backend.identity.member.command.RegisterLocalMemberV2Command;
import matchuri.backend.identity.member.command.UpdateMemberBasicInfoCommand;
import matchuri.backend.identity.member.command.UpdateMemberPasswordCommand;
import matchuri.backend.identity.member.command.UpdateMemberTasteProfileCommand;
import matchuri.backend.identity.member.result.CreateMemberResult;
import matchuri.backend.identity.member.result.MemberProfileResult;
import matchuri.backend.identity.member.result.MemberHomeResult;
import matchuri.backend.identity.member.result.MemberPresetProfileImageResult;
import matchuri.backend.identity.member.result.MemberProfileImageResult;
import matchuri.backend.identity.member.result.MemberLocationResult;
import matchuri.backend.identity.member.result.MemberTasteProfileSummaryResult;
import matchuri.backend.identity.member.result.MemberTasteUpdateResult;
import matchuri.backend.identity.member.result.RegisterLocalMemberResult;
import matchuri.backend.identity.member.result.UpdateMemberPasswordResult;
import matchuri.backend.identity.member.result.UpdateMemberResult;
import matchuri.backend.identity.member.result.WithdrawMemberResult;
import org.jspecify.annotations.Nullable;

public interface MemberService {

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    RegisterLocalMemberResult registerLocalMember(RegisterLocalMemberCommand command);

    RegisterLocalMemberResult registerLocalMemberV2(RegisterLocalMemberV2Command command);

    CreateMemberResult createMember(CreateMemberCommand command);

    MemberProfileResult getMyProfile(Long memberId);

    MemberHomeResult getHomeMember(Long memberId);

    List<MemberPresetProfileImageResult> getPresetProfileImages(Long memberId);

    MemberProfileImageResult setPresetProfileImage(Long memberId, Long presetProfileImageId);

    @Nullable MemberLocationResult getMyLocation(Long memberId);

    MemberLocationResult putMyLocation(Long memberId, PutMemberLocationCommand command);

    MemberTasteProfileSummaryResult getMyTasteProfile(Long memberId);

    UpdateMemberResult updateMyProfile(Long memberId, UpdateMemberBasicInfoCommand command);

    UpdateMemberPasswordResult updateMyPassword(Long memberId, UpdateMemberPasswordCommand command);

    MemberTasteUpdateResult updateMyTasteProfile(Long memberId, UpdateMemberTasteProfileCommand command);

    WithdrawMemberResult withdraw(Long memberId);
}
