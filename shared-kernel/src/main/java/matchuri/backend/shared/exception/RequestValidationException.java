package matchuri.backend.shared.exception;

import matchuri.backend.shared.persistence.CommonErrorCode;
import matchuri.backend.shared.api.ValidationErrorDetail;

public class RequestValidationException extends MatchuriException {

    private final ValidationErrorDetail detail;

    private RequestValidationException(ErrorCode errorCode, ValidationErrorDetail detail) {
        super(errorCode);
        this.detail = detail;
    }

    public static RequestValidationException invalidPathVariable(String field, String reason) {
        return new RequestValidationException(
                CommonErrorCode.INVALID_PATH_VARIABLE,
                new ValidationErrorDetail("PATH", field, reason)
        );
    }

    public static RequestValidationException invalidBodyField(String field, String reason) {
        return new RequestValidationException(
                CommonErrorCode.INVALID_BODY_FIELD,
                new ValidationErrorDetail("BODY", field, reason)
        );
    }

    public ValidationErrorDetail getDetail() {
        return detail;
    }
}
