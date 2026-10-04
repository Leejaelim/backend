package matchuri.backend.identity.api;

import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.exception.MemberErrorCode;
import matchuri.backend.identity.member.repository.MemberRepository;
import matchuri.backend.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReader {

    private final MemberRepository memberRepository;

    public Member getActiveMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND, memberId));

        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BusinessException(MemberErrorCode.INACTIVE_MEMBER, member.getId());
        }

        return member;
    }
}
