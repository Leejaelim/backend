package matchuri.backend.identity.member.support.deletion;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.auth.repository.AuthExchangeCodeRepository;
import matchuri.backend.identity.auth.repository.AuthRefreshTokenRepository;
import matchuri.backend.identity.member.event.MemberWithdrawn;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.exception.MemberErrorCode;
import matchuri.backend.identity.member.repository.MemberRepository;
import matchuri.backend.shared.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationEventPublisher;

@Component
@RequiredArgsConstructor
public class MemberWithdrawalManager {

    @Value("${matchuri.member-deletion.grace-period-day}")
    public int DELETION_GRACE_PERIOD_DAYS;

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;
    private final AuthExchangeCodeRepository authExchangeCodeRepository;

    public Member withdraw(Long memberId, LocalDateTime deletedAt) {
        Member member = memberRepository.findByIdForUpdate(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND, memberId));

        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BusinessException(MemberErrorCode.INACTIVE_MEMBER, member.getId());
        }

        member.withdraw(deletedAt, deletedAt.plusDays(DELETION_GRACE_PERIOD_DAYS));
        eventPublisher.publishEvent(new MemberWithdrawn(memberId, deletedAt));
        authRefreshTokenRepository.deleteByMemberId(memberId);
        authExchangeCodeRepository.deleteByMemberId(memberId);

        return member;
    }
}
