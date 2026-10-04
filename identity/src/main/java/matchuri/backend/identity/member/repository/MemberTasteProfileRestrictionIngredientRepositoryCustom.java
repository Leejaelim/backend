package matchuri.backend.identity.member.repository;

import java.util.List;
import matchuri.backend.identity.member.entity.MemberTasteProfileRestrictionIngredient;

public interface MemberTasteProfileRestrictionIngredientRepositoryCustom {

    List<MemberTasteProfileRestrictionIngredient> findAllByProfileIdOrderByDisplay(Long profileId);
}
