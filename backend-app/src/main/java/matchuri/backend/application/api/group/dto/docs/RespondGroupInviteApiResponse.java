package matchuri.backend.application.api.group.dto.docs;

import matchuri.backend.application.api.group.dto.response.RespondGroupInviteResponse;
import matchuri.backend.shared.api.ErrorResponse;

public record RespondGroupInviteApiResponse(
        boolean success,
        RespondGroupInviteResponse data,
        ErrorResponse error
) {
}
