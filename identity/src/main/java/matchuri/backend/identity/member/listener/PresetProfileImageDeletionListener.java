package matchuri.backend.identity.member.listener;

import lombok.RequiredArgsConstructor;
import matchuri.backend.media.event.PresetProfileImageDeleted;
import matchuri.backend.identity.member.repository.MemberProfileImageRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PresetProfileImageDeletionListener {
    private final MemberProfileImageRepository repository;

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    public void on(PresetProfileImageDeleted event) {
        repository.updateToDefault(event.deletedAssetId(), event.defaultAssetId());
    }
}
