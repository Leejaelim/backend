package matchuri.backend.identity.member.repository.impl;

import static matchuri.backend.media.entity.QImageAsset.imageAsset;
import static matchuri.backend.identity.member.entity.QMemberProfileImage.memberProfileImage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.media.entity.ImageAsset;
import matchuri.backend.identity.member.entity.MemberProfileImage;
import matchuri.backend.identity.member.repository.MemberProfileImageRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberProfileImageRepositoryImpl implements MemberProfileImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    public List<MemberProfileImage> findAllByMemberIdIn(List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return List.of();
        }

        return jpaQueryFactory
                .selectFrom(memberProfileImage)
                .join(memberProfileImage.imageAsset, imageAsset).fetchJoin()
                .where(memberProfileImage.member.id.in(memberIds))
                .fetch();
    }

    @Override
    public int updateToDefault(Long deletedAssetId, Long updateAssetId) {
        entityManager.flush();
        ImageAsset replacement = entityManager.getReference(ImageAsset.class, updateAssetId);
        long updatedCount = jpaQueryFactory
                .update(memberProfileImage)
                .set(memberProfileImage.imageAsset, replacement)
                .where(memberProfileImage.imageAsset.id.eq(deletedAssetId))
                .execute();
        entityManager.clear();
        return Math.toIntExact(updatedCount);
    }
}
