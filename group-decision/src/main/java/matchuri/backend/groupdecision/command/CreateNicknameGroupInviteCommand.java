package matchuri.backend.groupdecision.command;

public record CreateNicknameGroupInviteCommand(
        Long groupId,
        String nickname
) {
}
