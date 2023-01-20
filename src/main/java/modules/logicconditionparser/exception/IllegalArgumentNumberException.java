package modules.logicconditionparser.exception;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.beans.ConstructorProperties;

public class IllegalArgumentNumberException extends ExecuteException {
    public IllegalArgumentNumberException() {
    }

    public IllegalArgumentNumberException(String message) {
        super(message);
    }

    public IllegalArgumentNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentNumberException(Throwable cause) {
        super(cause);
    }

    public IllegalArgumentNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
