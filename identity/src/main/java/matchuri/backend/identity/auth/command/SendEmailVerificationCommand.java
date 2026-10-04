package matchuri.backend.identity.auth.command;

import matchuri.backend.identity.auth.api.EmailVerificationPurpose;

public record SendEmailVerificationCommand(
        String email,
        EmailVerificationPurpose purpose,
        String loginId
) {
    public SendEmailVerificationCommand {
        email = email == null ? null : email.trim().toLowerCase();
        loginId = purpose == EmailVerificationPurpose.RESET_PASSWORD && loginId != null && !loginId.isBlank()
                ? loginId.trim()
                : null;
    }
}
