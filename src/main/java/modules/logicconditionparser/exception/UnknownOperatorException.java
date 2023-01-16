package modules.logicconditionparser.exception;

import lombok.AllArgsConstructor;

public class UnknownOperatorException extends Exception{
    public UnknownOperatorException() {
    }

    public UnknownOperatorException(String message) {
        super(message);
    }

    public UnknownOperatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownOperatorException(Throwable cause) {
        super(cause);
    }

    public UnknownOperatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
