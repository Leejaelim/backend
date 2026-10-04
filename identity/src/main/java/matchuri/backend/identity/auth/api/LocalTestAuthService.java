package matchuri.backend.identity.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchuri.backend.identity.auth.result.IssuedAccessToken;
import matchuri.backend.identity.auth.result.LoginPayload;
import matchuri.backend.identity.auth.support.token.JwtTokenProvider;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.api.MemberReader;
import matchuri.backend.identity.member.support.onboarding.OnboardingStatusResolver;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Profile("local & !dev & !prod")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalTestAuthService {

    private final MemberReader memberReader;
    private final JwtTokenProvider jwtTokenProvider;
    private final OnboardingStatusResolver onboardingStatusResolver;

    public LoginPayload login(Long memberId) {
        Member member = memberReader.getActiveMember(memberId);
        IssuedAccessToken issuedAccessToken = jwtTokenProvider.issueAccessToken(member);

        log.info("auth event=test_login_success memberId={}", member.getId());
        return LoginPayload.from(issuedAccessToken, member, onboardingStatusResolver.resolve(member));
    }
}
