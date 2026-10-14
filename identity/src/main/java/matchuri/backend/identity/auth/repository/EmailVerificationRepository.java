package matchuri.backend.identity.auth.repository;

import java.util.Optional;
import matchuri.backend.identity.auth.entity.EmailVerification;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long>,
        EmailVerificationRepositoryCustom {

    Optional<EmailVerification> findByVerificationTokenHash(String verificationTokenHash);

}
