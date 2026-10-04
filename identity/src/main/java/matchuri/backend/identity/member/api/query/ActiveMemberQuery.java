package matchuri.backend.identity.member.api.query;

import java.util.Optional;
import matchuri.backend.identity.member.entity.Member;

public interface ActiveMemberQuery {
    Optional<Member> findByActiveMemberByNickname(String nickname);
}
