package matchuri.backend.application.api.common.dto.docs;

import io.swagger.v3.oas.annotations.media.Schema;
import matchuri.backend.application.api.common.dto.response.HomeResponse;
import matchuri.backend.shared.api.ErrorResponse;

@Schema(description = "홈 조회 응답 envelope입니다.")
public record HomeApiResponse(boolean success, HomeResponse data, ErrorResponse error) {}
