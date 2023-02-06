package thedivazo.conditionhandler.exception;

public class UnknownConditionException extends ConditionException{
    public UnknownConditionException(String message) {
        super(message);
    }

    public UnknownConditionException(String message, int position, String invalidCode) {
        super(message, position, invalidCode);
    }
}
