package matchuri.backend.identity.member.command;

public record CreateMemberCommand(
        String loginId,
        String password
) {
}
