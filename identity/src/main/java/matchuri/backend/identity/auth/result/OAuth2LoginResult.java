package matchuri.backend.identity.auth.result;

public record OAuth2LoginResult(
        Long memberId,
        String refreshToken,
        String exchangeCode
) {
}
