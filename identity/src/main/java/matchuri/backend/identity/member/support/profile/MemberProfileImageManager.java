package matchuri.backend.identity.member.support.profile;

import java.util.List;
import lombok.RequiredArgsConstructor;
import matchuri.backend.media.entity.PresetProfileImage;
import matchuri.backend.media.exception.ImageErrorCode;
import matchuri.backend.media.api.query.PresetProfileImageQuery;
import matchuri.backend.identity.member.entity.Member;
import matchuri.backend.identity.member.entity.MemberProfileImage;
import matchuri.backend.identity.member.repository.MemberProfileImageRepository;
import matchuri.backend.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberProfileImageManager {

    private final PresetProfileImageQuery presetProfileImageRepository;
    private final MemberProfileImageRepository memberProfileImageRepository;

    public MemberProfileImage initializeDefault(Member member) {
        return memberProfileImageRepository.findByMemberId(member.getId())
                .orElseGet(() -> memberProfileImageRepository.save(
                        new MemberProfileImage(member, getSingleDefault().getImageAsset())
                ));
    }

    public SelectedPresetProfileImage selectPreset(Member member, Long presetProfileImageId) {
        PresetProfileImage preset = presetProfileImageRepository.findActiveById(presetProfileImageId)
                .orElseThrow(() -> new BusinessException(ImageErrorCode.PRESET_PROFILE_NOT_FOUND, presetProfileImageId));

        MemberProfileImage profileImage = memberProfileImageRepository.findByMemberId(member.getId())
                .map(existing -> replaceImage(existing, preset))
                .orElseGet(() -> memberProfileImageRepository.save(new MemberProfileImage(member, preset.getImageAsset())));

        return new SelectedPresetProfileImage(profileImage, preset);
    }

    private PresetProfileImage getSingleDefault() {
        List<PresetProfileImage> defaults = presetProfileImageRepository.findActiveDefaults();
        if (defaults.isEmpty()) {
            throw new BusinessException(ImageErrorCode.DEFAULT_PRESET_PROFILE_NOT_FOUND);
        }
        if (defaults.size() > 1) {
            throw new IllegalStateException("활성 기본 프리셋 프로필 이미지가 둘 이상입니다.");
        }
        return defaults.getFirst();
    }

    private MemberProfileImage replaceImage(MemberProfileImage existing, PresetProfileImage preset) {
        existing.replaceImageAsset(preset.getImageAsset());
        return existing;
    }

    public record SelectedPresetProfileImage(
            MemberProfileImage memberProfileImage,
            PresetProfileImage presetProfileImage
    ) {
    }
}
