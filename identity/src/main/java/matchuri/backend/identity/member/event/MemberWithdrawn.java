package matchuri.backend.identity.member.event;

import java.time.LocalDateTime;

public record MemberWithdrawn(Long memberId, LocalDateTime deletedAt) {}
