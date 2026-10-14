package matchuri.backend.identity.auth.api;

public interface CaptchaVerifier {
    boolean verify(String token, CaptchaPurpose purpose, String clientIp);
}
