package matchuri.backend.identity.auth.repository;

import java.util.List;
import matchuri.backend.identity.auth.entity.EmailVerification;
import matchuri.backend.identity.auth.api.EmailVerificationPurpose;
import matchuri.backend.identity.auth.entity.EmailVerificationStatus;
import org.jspecify.annotations.Nullable;

public interface EmailVerificationRepositoryCustom {

    List<EmailVerification> findAllByTargetAndStatus(String email, EmailVerificationPurpose purpose, @Nullable String loginId, EmailVerificationStatus status);
}
