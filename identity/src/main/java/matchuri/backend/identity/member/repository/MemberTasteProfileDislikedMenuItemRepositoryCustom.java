package matchuri.backend.identity.member.repository;

import java.util.List;
import matchuri.backend.identity.member.entity.MemberTasteProfileDislikedMenuItem;

public interface MemberTasteProfileDislikedMenuItemRepositoryCustom {

    List<MemberTasteProfileDislikedMenuItem> findAllByProfileIdOrderByDisplay(Long profileId);
}
