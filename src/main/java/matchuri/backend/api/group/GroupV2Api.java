package matchuri.backend.api.group;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import matchuri.backend.api.group.dto.docs.GroupApiExamples;
import matchuri.backend.api.group.dto.docs.GroupDetailV2ApiResponse;
import matchuri.backend.api.group.dto.response.GroupDetailV2Response;
import matchuri.backend.global.api.ApiResponse;
import matchuri.backend.global.security.AuthenticatedMemberId;

public interface GroupV2Api {

    @Operation(
            summary = "그룹 상세 조회 v2",
            description = """
                    그룹 방 상세와 현재 멤버, 가장 최근 그룹 추천 상태를 조회합니다.

                    - 로그인한 활성 회원만 사용할 수 있습니다.
                    - 현재 회원이 해당 그룹의 `ACTIVE` 멤버일 때만 조회할 수 있습니다.
                    - 그룹의 모든 `ACTIVE` 멤버에게 고정 초대 코드를 함께 반환합니다.
                    - 멤버 목록에는 각 회원의 `memberProfileImageUrl`이 포함되며, 이미지가 없으면 null입니다.
                    - 멤버 목록의 각 항목은 현재 로그인한 회원이면 `isMe=true`, 아니면 `false`를 반환합니다.
                    - 삭제된 그룹은 조회할 수 없습니다.
                    - 가장 최근 추천 세션이 있으면 `recentlyRecommendation`을 함께 반환합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupDetailV2ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "success",
                                    value = GroupApiExamples.GROUP_DETAIL_V2_SUCCESS
                            )
                    )
            )
    })
    ApiResponse<GroupDetailV2Response> getGroup(@AuthenticatedMemberId Long memberId, Long groupId);
}
