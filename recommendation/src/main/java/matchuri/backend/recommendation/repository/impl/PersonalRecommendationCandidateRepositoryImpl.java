package matchuri.backend.recommendation.repository.impl;

import static matchuri.backend.media.entity.QImageAsset.imageAsset;
import static matchuri.backend.catalog.entity.QMenuItem.menuItem;
import static matchuri.backend.catalog.entity.QMenuItemImage.menuItemImage;
import static matchuri.backend.recommendation.entity.QPersonalRecommendationCandidate.personalRecommendationCandidate;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.recommendation.repository.PersonalRecommendationCandidateQueryRow;
import matchuri.backend.recommendation.repository.PersonalRecommendationCandidateRepositoryCustom;
import matchuri.backend.recommendation.entity.PersonalRecommendationCandidate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PersonalRecommendationCandidateRepositoryImpl implements PersonalRecommendationCandidateRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PersonalRecommendationCandidateQueryRow> findCandidateRowsByPersonalRecommendationId(Long personalRecommendationId) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        PersonalRecommendationCandidateQueryRow.class,
                        personalRecommendationCandidate.id,
                        menuItem.id,
                        menuItem.name,
                        imageAsset.objectKey,
                        personalRecommendationCandidate.rankNo,
                        personalRecommendationCandidate.score
                ))
                .from(personalRecommendationCandidate)
                .join(personalRecommendationCandidate.menuItem, menuItem)
                .leftJoin(menuItemImage).on(menuItemImage.menu.eq(menuItem))
                .leftJoin(menuItemImage.imageAsset, imageAsset)
                .where(personalRecommendationCandidate.personalRecommendation.id.eq(personalRecommendationId))
                .orderBy(personalRecommendationCandidate.rankNo.asc())
                .fetch();
    }

    @Override
    public List<PersonalRecommendationCandidate> findRepresentativeCandidates(List<Long> personalRecommendationIds) {
        if (personalRecommendationIds.isEmpty()) {
            return List.of();
        }

        return jpaQueryFactory
                .selectFrom(personalRecommendationCandidate)
                .join(personalRecommendationCandidate.menuItem, menuItem).fetchJoin()
                .where(
                        personalRecommendationCandidate.personalRecommendation.id.in(personalRecommendationIds),
                        personalRecommendationCandidate.personalRecommendation.selectedCandidate.id
                                .eq(personalRecommendationCandidate.id)
                                .or(personalRecommendationCandidate.personalRecommendation.selectedCandidate.id.isNull()
                                        .and(personalRecommendationCandidate.rankNo.eq(1)))
                )
                .fetch();
    }
}
