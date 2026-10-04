package matchuri.backend.identity.member.result;

public record MemberPresetProfileImageResult(
        Long presetProfileImageId,
        String imageUrl,
        boolean isDefault
) {
}