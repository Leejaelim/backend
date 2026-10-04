package matchuri.backend.identity.member.repository;

import java.util.Optional;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.entity.SocialProviderType;
import org.jspecify.annotations.NullMarked;
import matchuri.backend.identity.member.api.query.ActiveMemberQuery;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface MemberRepository extends JpaRepository<Member, Long>, ActiveMemberQuery, MemberRepositoryCustom {

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    boolean existsByIdAndNicknameCompletedTrue(Long memberId);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByEmailAndSocialFalseAndStatus(String email, MemberStatus status);

    Optional<Member> findByLoginIdAndEmailAndSocialFalseAndStatus(String loginId, String email, MemberStatus status);

    Optional<Member> findBySocialProviderTypeAndSocialProviderUserId(SocialProviderType socialProviderType, String socialProviderUserId);

    boolean existsByEmailAndSocialFalse(String email);
}
