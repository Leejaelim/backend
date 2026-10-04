package matchuri.backend.identity.auth.repository;

import java.util.Optional;
import matchuri.backend.identity.auth.entity.AuthExchangeCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthExchangeCodeRepository extends JpaRepository<AuthExchangeCode, Long> {

    Optional<AuthExchangeCode> findByCode(String code);

    void deleteByMemberId(Long memberId);
}
