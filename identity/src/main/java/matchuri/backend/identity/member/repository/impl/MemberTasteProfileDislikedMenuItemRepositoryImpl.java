package matchuri.backend.identity.member.repository.impl;

import static matchuri.backend.identity.member.entity.QMemberTasteProfileDislikedMenuItem.memberTasteProfileDislikedMenuItem;
import static matchuri.backend.catalog.entity.QMenuItem.menuItem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.identity.member.entity.MemberTasteProfileDislikedMenuItem;
import matchuri.backend.identity.member.repository.MemberTasteProfileDislikedMenuItemRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberTasteProfileDislikedMenuItemRepositoryImpl
        implements MemberTasteProfileDislikedMenuItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberTasteProfileDislikedMenuItem> findAllByProfileIdOrderByDisplay(Long profileId) {
        return jpaQueryFactory
                .selectFrom(memberTasteProfileDislikedMenuItem)
                .join(memberTasteProfileDislikedMenuItem.menuItem, menuItem).fetchJoin()
                .where(memberTasteProfileDislikedMenuItem.profile.id.eq(profileId))
                .orderBy(menuItem.name.asc(), menuItem.id.asc())
                .fetch();
    }
}
