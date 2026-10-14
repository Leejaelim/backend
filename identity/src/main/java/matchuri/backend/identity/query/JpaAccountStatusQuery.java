package matchuri.backend.identity.query;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.api.AccountStatusQuery;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class JpaAccountStatusQuery implements AccountStatusQuery {
    private final MemberRepository repository;

    @Override
    public boolean isActiveMember(Long memberId) {
        return repository.findById(memberId).filter(member -> member.getStatus() == MemberStatus.ACTIVE).isPresent();
    }

    @Override
    public List<Long> activeMemberIds(Collection<Long> memberIds) {
        return repository.findAllById(memberIds).stream().filter(member -> member.getStatus() == MemberStatus.ACTIVE)
                .map(member -> member.getId()).toList();
    }
}
