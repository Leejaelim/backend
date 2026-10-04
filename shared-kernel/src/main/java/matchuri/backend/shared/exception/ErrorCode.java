package matchuri.backend.shared.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();

    HttpStatus getHttpStatus();

    String getMessage();

    String format(Object... args);

    String getDomainPrefix();

    default String getCode() {
        return getDomainPrefix() + ((Enum<?>) this).name();
    }
}
