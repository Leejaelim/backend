package matchuri.backend.identity.auth.api;

import matchuri.backend.identity.auth.command.FindLoginIdCommand;
import matchuri.backend.identity.auth.command.ResetPasswordCommand;
import matchuri.backend.identity.auth.result.FindLoginIdResult;
import matchuri.backend.identity.auth.result.ResetPasswordResult;

public interface AccountRecoveryService {

    FindLoginIdResult findLoginId(FindLoginIdCommand command);

    ResetPasswordResult resetPassword(ResetPasswordCommand command);
}
