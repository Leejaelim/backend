package matchuri.backend.shared.exception;

public class AuthorizationException extends MatchuriException {

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
