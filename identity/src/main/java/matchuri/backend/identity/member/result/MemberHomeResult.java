package matchuri.backend.identity.member.result;

import org.jspecify.annotations.Nullable;

public record MemberHomeResult(
        MemberProfileResult profile,
        @Nullable MemberLocationResult location,
        MemberTasteProfileSummaryResult tasteProfile
) {
}
