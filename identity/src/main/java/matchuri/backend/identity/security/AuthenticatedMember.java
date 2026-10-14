package matchuri.backend.identity.security;

import matchuri.backend.identity.member.entity.MemberRole;

public record AuthenticatedMember(
        Long memberId,
        String loginId,
        MemberRole role,
        String requiredAgreementRevision
) {
}
