package matchuri.backend.identity.member.repository;

import java.util.List;
import matchuri.backend.identity.member.entity.MemberTasteProfileCategory;

public interface MemberTasteProfileCategoryRepositoryCustom {

    List<MemberTasteProfileCategory> findAllByProfileIdOrderByDisplay(Long profileId);

    List<MemberTasteProfileAttributeCategoryRow> findAttributeCategoryRowsByProfileId(Long profileId);
}
