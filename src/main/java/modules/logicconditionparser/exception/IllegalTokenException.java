package modules.logicconditionparser.exception;

public class IllegalTokenException extends ExecuteException{
    public IllegalTokenException() {
    }

    public IllegalTokenException(String message) {
        super(message);
    }

    public IllegalTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTokenException(Throwable cause) {
        super(cause);
    }

    public IllegalTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
