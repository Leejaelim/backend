package matchuri.backend.identity.auth.api;

import matchuri.backend.identity.auth.command.ConfirmEmailVerificationCommand;
import matchuri.backend.identity.auth.command.SendEmailVerificationCommand;
import matchuri.backend.identity.auth.result.ConfirmEmailVerificationResult;
import matchuri.backend.identity.auth.result.SendEmailVerificationResult;

public interface EmailVerificationService {
    SendEmailVerificationResult sendVerificationEmail(SendEmailVerificationCommand command);

    ConfirmEmailVerificationResult confirmVerificationEmail(ConfirmEmailVerificationCommand command);
}
