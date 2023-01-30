package thedivazo.conditionhandler.exception;

/**
 * Родоначальник всех ошибок в моем парсере.
 * Имеет все конструкторы по умолчанию, может еще добавлю в него что-нибудь.
 */
public class ConditionException extends Exception {


    public ConditionException() {
    }

    public ConditionException(String message) {
        super(message);
    }

    public ConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConditionException(Throwable cause) {
        super(cause);
    }

    public ConditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
