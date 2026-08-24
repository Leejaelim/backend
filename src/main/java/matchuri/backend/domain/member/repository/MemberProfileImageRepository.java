package matchuri.backend.domain.member.repository;

import java.util.Optional;
import matchuri.backend.domain.member.entity.MemberProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberProfileImageRepository extends JpaRepository<MemberProfileImage, Long> {

    Optional<MemberProfileImage> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update MemberProfileImage mp
        set mp.imageAsset.id = :updateAssetId
            where mp.imageAsset.id = :deletedAssetId
    """)
    int updateToDefault(@Param("deletedAssetId") Long deletedAssetId, @Param("updateAssetId") Long updateAssetId);
}
