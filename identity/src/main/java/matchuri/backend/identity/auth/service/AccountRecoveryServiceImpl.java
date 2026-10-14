package matchuri.backend.identity.auth.service;

import matchuri.backend.identity.auth.api.AccountRecoveryService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.auth.command.FindLoginIdCommand;
import matchuri.backend.identity.auth.command.ResetPasswordCommand;
import matchuri.backend.identity.auth.entity.AuthRefreshToken;
import matchuri.backend.identity.auth.exception.AuthErrorCode;
import matchuri.backend.identity.auth.repository.AuthRefreshTokenRepository;
import matchuri.backend.identity.auth.result.FindLoginIdResult;
import matchuri.backend.identity.auth.result.ResetPasswordResult;
import matchuri.backend.identity.auth.support.verification.EmailVerificationTokenVerifier;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberStatus;
import matchuri.backend.identity.member.repository.MemberRepository;
import matchuri.backend.shared.exception.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountRecoveryServiceImpl implements AccountRecoveryService {

    private final EmailVerificationTokenVerifier emailVerificationTokenVerifier;
    private final MemberRepository memberRepository;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public FindLoginIdResult findLoginId(FindLoginIdCommand command) {

        String token = command.emailVerificationToken();
        var verification = emailVerificationTokenVerifier.verifyFindLoginIdToken(token);
        String email = verification.getEmail();

        Member member = memberRepository.findByEmailAndSocialFalseAndStatus(email, MemberStatus.ACTIVE)
                .orElseThrow(() -> new AuthenticationException(AuthErrorCode.EMAIL_VERIFICATION_FAILED));

        return new FindLoginIdResult(member.getLoginId());
    }

    @Override
    @Transactional
    public ResetPasswordResult resetPassword(ResetPasswordCommand command) {

        var verification = emailVerificationTokenVerifier.verifyResetPasswordToken(
                command.loginId(),
                command.emailVerificationToken()
        );

        Member member = memberRepository.findByLoginIdAndEmailAndSocialFalseAndStatus(
                        command.loginId(),
                        verification.getEmail(),
                        MemberStatus.ACTIVE
                )
                .orElseThrow(() -> new AuthenticationException(AuthErrorCode.EMAIL_VERIFICATION_FAILED));

        String newPassword = command.newPassword();
        String encoded = passwordEncoder.encode(newPassword);
        member.updatePasswordHash(encoded);

        List<AuthRefreshToken> oldRefreshTokens = authRefreshTokenRepository.findByMemberId(member.getId());
        authRefreshTokenRepository.deleteAll(oldRefreshTokens);

        return ResetPasswordResult.success();
    }
}
