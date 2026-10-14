package matchuri.backend.identity.member.repository;

import java.util.List;
import matchuri.backend.identity.member.entity.MemberTasteProfileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTasteProfileCategoryRepository extends JpaRepository<MemberTasteProfileCategory, Long>,
        MemberTasteProfileCategoryRepositoryCustom {

    List<MemberTasteProfileCategory> findAllByProfileId(Long profileId);

}
