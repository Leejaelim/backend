package matchuri.backend.shared.exception;

public class BusinessException extends MatchuriException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
