package matchuri.backend.domain.member.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import matchuri.backend.domain.member.entity.Member;
import static matchuri.backend.domain.member.entity.QMember.member;

import matchuri.backend.domain.member.entity.MemberStatus;
import matchuri.backend.domain.member.repository.MemberRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Member> findByActiveMemberByNickname(String nickname) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(member)
                        .from(member)
                        .where(
                                member.nickname.eq(nickname),
                                member.status.eq(MemberStatus.ACTIVE)
                        )
                        .fetchOne()
        );
    }
}
