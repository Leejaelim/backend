package matchuri.backend.application.api.auth.mapper;

import matchuri.backend.application.api.auth.dto.request.FindLoginIdRequest;
import matchuri.backend.application.api.auth.dto.request.ResetPasswordRequest;
import matchuri.backend.application.api.auth.dto.response.FindLoginIdResponse;
import matchuri.backend.application.api.auth.dto.response.ResetPasswordResponse;
import matchuri.backend.identity.auth.command.FindLoginIdCommand;
import matchuri.backend.identity.auth.command.ResetPasswordCommand;
import matchuri.backend.identity.auth.result.FindLoginIdResult;
import matchuri.backend.identity.auth.result.ResetPasswordResult;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public FindLoginIdCommand toFindLoginIdCommand(FindLoginIdRequest request) {
        return new FindLoginIdCommand(request.emailVerificationToken());
    }

    public FindLoginIdResponse toFindLoginIdResponse(FindLoginIdResult result) {
        return new FindLoginIdResponse(result.loginId());
    }

    public ResetPasswordCommand toResetPasswordCommand(ResetPasswordRequest request) {
        return new ResetPasswordCommand(
                request.loginId(),
                request.emailVerificationToken(),
                request.newPassword()
        );
    }

    public ResetPasswordResponse toResetPasswordResponse(ResetPasswordResult result) {
        return new ResetPasswordResponse(result.reset());
    }
}
