package matchuri.backend.application.api.group.dto.docs;

import io.swagger.v3.oas.annotations.media.Schema;
import matchuri.backend.application.api.group.dto.response.DeleteGroupResponse;
import matchuri.backend.shared.api.ErrorResponse;

@Schema(description = "그룹 삭제 API의 공통 응답 envelope입니다.")
public record DeleteGroupApiResponse(boolean success, DeleteGroupResponse data, ErrorResponse error) {
}
