package matchuri.backend.identity.auth.result;

public record ResetPasswordResult(
        boolean reset
) {
    public static ResetPasswordResult success() {
        return new ResetPasswordResult(true);
    }
}
