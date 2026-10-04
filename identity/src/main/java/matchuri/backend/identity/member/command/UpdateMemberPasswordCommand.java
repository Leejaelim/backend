package matchuri.backend.identity.member.command;

public record UpdateMemberPasswordCommand(
        String currentPassword,
        String newPassword
) {
}
