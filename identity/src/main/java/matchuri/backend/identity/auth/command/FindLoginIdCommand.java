package matchuri.backend.identity.auth.command;

public record FindLoginIdCommand(
        String emailVerificationToken
) {
    public FindLoginIdCommand {
        emailVerificationToken = emailVerificationToken == null ? null : emailVerificationToken.trim();
    }
}
