package matchuri.backend.identity.member.result;

import java.math.BigDecimal;
import matchuri.backend.identity.member.entity.MemberLocation;

public record MemberLocationResult(
        BigDecimal latitude,
        BigDecimal longitude,
        Integer radiusMeters,
        String address
) {
    public static MemberLocationResult from(MemberLocation location) {
        return new MemberLocationResult(
                location.getLatitude(),
                location.getLongitude(),
                location.getRadiusMeters(),
                location.getAddress()
        );
    }
}
