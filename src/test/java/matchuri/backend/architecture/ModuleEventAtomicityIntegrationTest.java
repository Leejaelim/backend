package matchuri.backend.architecture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import java.time.LocalDateTime;
import matchuri.backend.groupdecision.entity.GroupRoom;
import matchuri.backend.groupdecision.entity.GroupRoomStatus;
import matchuri.backend.groupdecision.entity.GroupMemberRole;
import matchuri.backend.groupdecision.entity.GroupInvite;
import matchuri.backend.groupdecision.repository.GroupInviteRepository;
import matchuri.backend.groupdecision.api.GroupAudienceQuery;
import matchuri.backend.identity.api.AccountStatusQuery;
import matchuri.backend.groupdecision.listener.MemberWithdrawalListener;
import matchuri.backend.groupdecision.repository.GroupRoomRepository;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberProfileImage;
import matchuri.backend.identity.member.entity.MemberRole;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.listener.PresetProfileImageDeletionListener;
import matchuri.backend.identity.member.repository.MemberProfileImageRepository;
import matchuri.backend.identity.member.repository.MemberRepository;
import matchuri.backend.identity.member.support.deletion.MemberWithdrawalManager;
import matchuri.backend.media.api.PresetProfileImageAdminService;
import matchuri.backend.media.api.storage.ObjectStorageClient;
import matchuri.backend.media.entity.ImageAsset;
import matchuri.backend.media.entity.ImageStorageProvider;
import matchuri.backend.media.entity.PresetProfileImage;
import matchuri.backend.media.repository.ImageAssetRepository;
import matchuri.backend.media.repository.PresetProfileImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:module-event-atomicity;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
@ActiveProfiles("test")
class ModuleEventAtomicityIntegrationTest {
    @Autowired MemberRepository members;
    @Autowired MemberProfileImageRepository profiles;
    @Autowired GroupRoomRepository rooms;
    @Autowired GroupInviteRepository invites;
    @Autowired ImageAssetRepository assets;
    @Autowired PresetProfileImageRepository presets;
    @Autowired MemberWithdrawalManager withdrawal;
    @Autowired PresetProfileImageAdminService presetService;
    @Autowired PlatformTransactionManager transactions;
    @Autowired GroupAudienceQuery groupAudience;
    @Autowired AccountStatusQuery accountStatus;
    @MockitoBean ObjectStorageClient storage;
    @MockitoSpyBean MemberWithdrawalListener withdrawalListener;
    @MockitoSpyBean PresetProfileImageDeletionListener imageListener;

    @AfterEach
    void cleanUp() {
        rooms.deleteAll();
        profiles.deleteAll();
        members.deleteAll();
        presets.deleteAll();
        assets.deleteAll();
    }

    @Test
    void onlyCurrentPendingInvitesInExistingGroupsCanBeDelivered() {
        Member owner = member("invite-owner");
        Member target = member("invite-target");
        GroupRoom room = rooms.save(GroupRoom.createOwnedBy("invites", "invite-code", owner));
        GroupInvite pending = invites.save(new GroupInvite(room, owner, target, LocalDateTime.now().plusHours(1)));
        GroupInvite expired = invites.save(new GroupInvite(room, owner, target, LocalDateTime.now().minusHours(1)));
        assertThat(groupAudience.isPendingInvite(pending.getId(), target.getId())).isTrue();
        assertThat(groupAudience.isPendingInvite(pending.getId(), owner.getId())).isFalse();
        assertThat(groupAudience.isPendingInvite(expired.getId(), target.getId())).isFalse();
        new TransactionTemplate(transactions).executeWithoutResult(status -> invites.findById(pending.getId()).orElseThrow().revoke());
        assertThat(groupAudience.isPendingInvite(pending.getId(), target.getId())).isFalse();
        GroupInvite beforeDeletion = invites.save(new GroupInvite(room, owner, target, LocalDateTime.now().plusHours(1)));
        new TransactionTemplate(transactions).executeWithoutResult(status -> rooms.findById(room.getId()).orElseThrow().delete(LocalDateTime.now()));
        assertThat(groupAudience.isPendingInvite(beforeDeletion.getId(), target.getId())).isFalse();
    }

    @Test
    void committedDeparturesWithdrawalsAndDeletionChangeEventRecipients() {
        Member owner = member("audience-owner");
        Member departing = member("audience-departing");
        Member withdrawn = member("audience-withdrawn");
        GroupRoom room = GroupRoom.createOwnedBy("audience", "audience-code", owner);
        room.addGroupMember(departing, GroupMemberRole.MEMBER);
        room.addGroupMember(withdrawn, GroupMemberRole.MEMBER);
        rooms.save(room);
        assertThat(groupAudience.activeMemberIds(room.getId()))
                .containsExactlyInAnyOrder(owner.getId(), departing.getId(), withdrawn.getId());
        assertThat(groupAudience.isActiveOwner(room.getId(), owner.getId())).isTrue();
        assertThat(groupAudience.isActiveOwner(room.getId(), departing.getId())).isFalse();
        new TransactionTemplate(transactions).executeWithoutResult(status -> {
            rooms.findById(room.getId()).orElseThrow().getGroupRoomMemberById(departing.getId())
                    .orElseThrow().leave(LocalDateTime.now());
            members.findById(withdrawn.getId()).orElseThrow()
                    .withdraw(LocalDateTime.now(), LocalDateTime.now().plusDays(3));
        });
        assertThat(groupAudience.activeMemberIds(room.getId())).containsExactly(owner.getId());
        assertThat(groupAudience.hasActiveMembership(room.getId(), departing.getId())).isFalse();
        assertThat(accountStatus.isActiveMember(withdrawn.getId())).isFalse();
        assertThat(accountStatus.activeMemberIds(java.util.List.of(owner.getId(), withdrawn.getId())))
                .containsExactly(owner.getId());
        new TransactionTemplate(transactions).executeWithoutResult(status ->
                rooms.findById(room.getId()).orElseThrow().delete(LocalDateTime.now()));
        assertThat(groupAudience.activeMemberIds(room.getId())).isEmpty();
        assertThat(groupAudience.isActiveOwner(room.getId(), owner.getId())).isFalse();
    }

    @Test
    void withdrawalAndOwnedGroupDeletionCommitTogether() {
        Member member = member("atomic-withdraw");
        GroupRoom room = rooms.save(GroupRoom.createOwnedBy("group", "atomic-code", member));
        new TransactionTemplate(transactions).executeWithoutResult(status -> withdrawal.withdraw(member.getId(), LocalDateTime.now()));
        assertThat(members.findById(member.getId()).orElseThrow().getStatus()).isEqualTo(MemberStatus.DELETED);
        assertThat(rooms.findById(room.getId()).orElseThrow().getStatus()).isEqualTo(GroupRoomStatus.DELETED);
    }

    @Test
    void failingWithdrawalListenerRollsBackBothModules() {
        Member member = member("failed-withdraw");
        GroupRoom room = rooms.save(GroupRoom.createOwnedBy("group", "failed-code", member));
        MemberWithdrawalListener target = AopTestUtils.getUltimateTargetObject(withdrawalListener);
        doAnswer(call -> { call.callRealMethod(); throw new IllegalStateException("listener failed"); })
                .when(target).on(any());
        assertThatThrownBy(() -> new TransactionTemplate(transactions).executeWithoutResult(
                status -> withdrawal.withdraw(member.getId(), LocalDateTime.now())))
                .isInstanceOf(IllegalStateException.class);
        assertThat(members.findById(member.getId()).orElseThrow().getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(rooms.findById(room.getId()).orElseThrow().getStatus()).isEqualTo(GroupRoomStatus.ACTIVE);
    }

    @Test
    void presetDeletionReassignsAllUsersBeforeSuccess() {
        PresetProfileImage fallback = preset("fallback.png", true);
        PresetProfileImage deleted = preset("selected.png", false);
        profiles.save(new MemberProfileImage(member("first-profile"), deleted.getImageAsset()));
        profiles.save(new MemberProfileImage(member("second-profile"), deleted.getImageAsset()));
        presetService.delete(deleted.getId());
        assertThat(presets.findById(deleted.getId()).orElseThrow().isDeleted()).isTrue();
        assertThat(profiles.findAll()).hasSize(2).allSatisfy(profile ->
                assertThat(profile.getImageAsset().getId()).isEqualTo(fallback.getImageAsset().getId()));
    }

    @Test
    void failingImageListenerRollsBackPresetAndProfileChanges() {
        preset("fallback.png", true);
        PresetProfileImage selected = preset("selected.png", false);
        Member member = member("failed-profile");
        profiles.save(new MemberProfileImage(member, selected.getImageAsset()));
        PresetProfileImageDeletionListener target = AopTestUtils.getUltimateTargetObject(imageListener);
        doAnswer(call -> { call.callRealMethod(); throw new IllegalStateException("listener failed"); })
                .when(target).on(any());
        assertThatThrownBy(() -> presetService.delete(selected.getId()))
                .isInstanceOf(IllegalStateException.class);
        assertThat(presets.findById(selected.getId()).orElseThrow().isDeleted()).isFalse();
        assertThat(profiles.findByMemberId(member.getId()).orElseThrow().getImageAsset().getId())
                .isEqualTo(selected.getImageAsset().getId());
    }

    private Member member(String loginId) {
        return members.save(new Member(loginId, "hashed-password", null, false, null, null,
                MemberRole.MEMBER, MemberStatus.ACTIVE));
    }

    private PresetProfileImage preset(String key, boolean isDefault) {
        ImageAsset asset = assets.save(new ImageAsset(ImageStorageProvider.CLOUDFLARE_R2,
                "test-bucket", key, key, "image/png", 1024, "a".repeat(64), 320, 320));
        return presets.save(new PresetProfileImage(asset, isDefault));
    }
}
