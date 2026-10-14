package matchuri.backend.identity.auth.command;

public record LoginCommand(
        String loginId,
        String password,
        String captchaToken
) {
}
