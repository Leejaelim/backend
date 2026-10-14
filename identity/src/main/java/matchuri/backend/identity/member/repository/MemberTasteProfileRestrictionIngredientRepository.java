package matchuri.backend.identity.member.repository;

import java.util.List;
import matchuri.backend.identity.member.entity.MemberTasteProfileRestrictionIngredient;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface MemberTasteProfileRestrictionIngredientRepository extends
        JpaRepository<MemberTasteProfileRestrictionIngredient, Long>,
        MemberTasteProfileRestrictionIngredientRepositoryCustom {

    List<MemberTasteProfileRestrictionIngredient> findAllByProfileId(Long profileId);

}
