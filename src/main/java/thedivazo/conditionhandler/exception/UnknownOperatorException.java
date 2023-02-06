package thedivazo.conditionhandler.exception;

import thedivazo.conditionhandler.condition.Condition;

public class UnknownOperatorException extends ConditionException {
    public UnknownOperatorException(String message) {
        super(message);
    }

    public UnknownOperatorException(String message, int position, String invalidCode) {
        super(message, position, invalidCode);
    }
}
