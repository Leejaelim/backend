package matchuri.backend.identity.member.result;

import java.time.LocalDateTime;
import matchuri.backend.identity.member.entity.Member;

public record CreateMemberResult(
        Long memberId,
        String loginId,
        LocalDateTime createdAt
) {

    public static CreateMemberResult from(Member member) {
        return new CreateMemberResult(
                member.getId(),
                member.getLoginId(),
                member.getCreatedAt()
        );
    }
}
