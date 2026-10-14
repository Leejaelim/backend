package matchuri.backend.application.api.group.dto.docs;

import matchuri.backend.application.api.group.dto.response.CreateNicknameGroupInviteResponse;
import matchuri.backend.shared.api.ErrorResponse;

public record CreateNicknameGroupInviteApiResponse(
        boolean success,
        CreateNicknameGroupInviteResponse data,
        ErrorResponse error
) {
}
