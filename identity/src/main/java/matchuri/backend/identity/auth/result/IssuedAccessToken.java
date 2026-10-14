package matchuri.backend.identity.auth.result;

public record IssuedAccessToken(
        String accessToken,
        long expiresIn
) {
}
