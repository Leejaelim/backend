package matchuri.backend.groupdecision.repository.impl;

import static matchuri.backend.groupdecision.entity.QGroupRecommendationVote.groupRecommendationVote;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.groupdecision.repository.GroupCandidateVoteCountRow;
import matchuri.backend.groupdecision.repository.GroupRecommendationVoteQueryRow;
import matchuri.backend.groupdecision.repository.GroupRecommendationVoteRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupRecommendationVoteRepositoryImpl implements GroupRecommendationVoteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GroupRecommendationVoteQueryRow> findVoteRowsByRecommendationId(Long recommendationId) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        GroupRecommendationVoteQueryRow.class,
                        groupRecommendationVote.member.id,
                        groupRecommendationVote.candidate.id
                ))
                .from(groupRecommendationVote)
                .where(groupRecommendationVote.groupRecommendation.id.eq(recommendationId))
                .fetch();
    }

    @Override
    public List<GroupCandidateVoteCountRow> countVotesByCandidateId(Long recommendationId) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        GroupCandidateVoteCountRow.class,
                        groupRecommendationVote.candidate.id,
                        groupRecommendationVote.id.count()
                ))
                .from(groupRecommendationVote)
                .where(groupRecommendationVote.groupRecommendation.id.eq(recommendationId))
                .groupBy(groupRecommendationVote.candidate.id)
                .fetch();
    }
}
