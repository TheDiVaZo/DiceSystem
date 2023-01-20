package modules.logicconditionparser.exception;

import lombok.AllArgsConstructor;


public class IllegalConditionException extends ExecuteException {
    public IllegalConditionException() {
    }

    public IllegalConditionException(String message) {
        super(message);
    }

    public IllegalConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalConditionException(Throwable cause) {
        super(cause);
    }

    public IllegalConditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
