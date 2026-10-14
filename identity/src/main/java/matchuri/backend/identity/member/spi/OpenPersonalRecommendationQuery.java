package matchuri.backend.identity.member.spi;

import java.util.Optional;

/** Read port used when saving taste preferences; it deliberately does not expire recommendations. */
public interface OpenPersonalRecommendationQuery {
    Optional<Long> findOpenPersonalRecommendationId(Long memberId);
}
