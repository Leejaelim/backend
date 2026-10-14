package matchuri.backend.identity.auth.service;

import matchuri.backend.identity.auth.api.AuthService;
import matchuri.backend.identity.auth.api.CaptchaVerifier;
import matchuri.backend.identity.auth.api.CaptchaPurpose;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchuri.backend.identity.auth.command.LoginCommand;
import matchuri.backend.identity.auth.command.OAuth2ExchangeCommand;
import matchuri.backend.identity.auth.exception.AuthErrorCode;
import matchuri.backend.identity.auth.result.IssuedAccessToken;
import matchuri.backend.identity.auth.result.LoginPayload;
import matchuri.backend.identity.auth.result.LoginResult;
import matchuri.backend.identity.auth.result.LogoutResult;
import matchuri.backend.identity.auth.result.TokenPair;
import matchuri.backend.identity.auth.support.token.JwtTokenProvider;
import matchuri.backend.identity.auth.support.token.SessionTokenService;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.entity.SocialProviderType;
import matchuri.backend.identity.member.exception.MemberErrorCode;
import matchuri.backend.identity.member.repository.MemberRepository;
import matchuri.backend.identity.member.support.onboarding.OnboardingStatusResolver;
import matchuri.backend.shared.exception.AuthenticationException;
import matchuri.backend.shared.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionTokenService sessionTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OnboardingStatusResolver onboardingStatusResolver;
    private final CaptchaVerifier captchaVerifier;

    @Override
    @Transactional
    public LoginResult login(LoginCommand command, String clientIp) {
        if (!captchaVerifier.verify(command.captchaToken(), CaptchaPurpose.LOGIN, clientIp)) {
            log.warn("auth event=captcha_rejected purpose={} ip={}", CaptchaPurpose.LOGIN, clientIp);
            throw new BusinessException(AuthErrorCode.CAPTCHA_VERIFICATION_FAILED);
        }

        Member member = memberRepository.findByLoginId(command.loginId())
                .orElseThrow(() -> new AuthenticationException(AuthErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(command.password(), member.getPasswordHash())) {
            throw new AuthenticationException(AuthErrorCode.LOGIN_FAILED);
        }

        ensureActive(member);

        TokenPair tokenPair = sessionTokenService.issueLoginTokenPair(member);
        log.info("auth event=login_success provider=local memberId={} ip={}", member.getId(), clientIp);

        return LoginResult.from(tokenPair, member, onboardingStatusResolver.resolve(member));
    }

    @Override
    @Transactional
    public LoginResult refresh(String refreshToken, String clientIp) {
        Member member = sessionTokenService.validateRefreshToken(refreshToken);
        ensureActive(member);
        TokenPair tokenPair = sessionTokenService.rotateRefreshToken(refreshToken, member);

        log.info("auth event=refresh_success memberId={} ip={}", member.getId(), clientIp);

        return LoginResult.from(tokenPair, member, onboardingStatusResolver.resolve(member));
    }

    @Override
    @Transactional
    public LogoutResult logout(Long memberId, String refreshToken, String clientIp) {
        sessionTokenService.revokeRefreshToken(refreshToken);
        log.info("auth event=logout provider=local memberId={} ip={}", memberId, clientIp);

        return new LogoutResult(true);
    }

    @Override
    public SocialProviderType resolveOAuth2LoginProvider(String provider) {
        SocialProviderType socialProviderType = SocialProviderType.fromRegistrationId(provider);
        if (!socialProviderType.isOAuth2LoginSupported()) {
            throw new AuthenticationException(AuthErrorCode.OAUTH2_PROVIDER_NOT_SUPPORTED);
        }
        return socialProviderType;
    }

    @Override
    @Transactional
    public LoginPayload exchangeOAuth2Code(OAuth2ExchangeCommand command, String clientIp) {
        if (!command.provider().isOAuth2LoginSupported()) {
            throw new AuthenticationException(AuthErrorCode.OAUTH2_PROVIDER_NOT_SUPPORTED);
        }

        Member member = sessionTokenService.consumeExchangeCode(command.provider(), command.code());
        ensureActive(member);

        IssuedAccessToken issuedAccessToken = jwtTokenProvider.issueAccessToken(member);
        log.info(
                "auth event=oauth2_exchange_success provider={} memberId={} ip={}",
                command.provider().name().toLowerCase(),
                member.getId(),
                clientIp
        );

        return LoginPayload.from(issuedAccessToken, member, onboardingStatusResolver.resolve(member));
    }

    private void ensureActive(Member member) {
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BusinessException(MemberErrorCode.INACTIVE_MEMBER, member.getId());
        }
    }
}
