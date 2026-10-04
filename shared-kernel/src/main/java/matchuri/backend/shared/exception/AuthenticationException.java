package matchuri.backend.shared.exception;

public class AuthenticationException extends MatchuriException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
