package matchuri.backend.identity.member.command;

public record RegisterLocalMemberV2Command(
        RegisterLocalMemberCommand member,
        UpdateMemberTasteProfileCommand tasteProfile
) {
}
