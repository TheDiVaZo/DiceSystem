package thedivazo.conditionhandler.exception;

import java.util.regex.Pattern;

public class FanoConditionException extends ConditionException {
    public FanoConditionException(String regEx1, String regEx2) {
        super(String.format("RegEx '%s' and regEx '%s' do not satisfy the Fano condition.", (regEx1), (regEx2)));
    }
}
