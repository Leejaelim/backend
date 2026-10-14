package matchuri.backend.identity.security;

import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberRole;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByLoginId(username)
                .filter(this::isLoginPasswordAdmin)
                .orElseThrow(() -> new UsernameNotFoundException("Admin member not found."));

        return User.withUsername(member.getLoginId())
                .password(member.getPasswordHash())
                .roles(member.getMemberRole().name())
                .build();
    }

    private boolean isLoginPasswordAdmin(Member member) {
        return member.getMemberRole() == MemberRole.ADMIN
                && member.getStatus() == MemberStatus.ACTIVE
                && !member.isSocial()
                && member.getPasswordHash() != null
                && !member.getPasswordHash().isBlank();
    }
}
