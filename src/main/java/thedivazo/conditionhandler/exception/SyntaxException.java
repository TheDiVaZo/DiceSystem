package thedivazo.conditionhandler.exception;

public class SyntaxException extends ConditionException {


    /**
     * @param invalidCharacters Неизвестный токен
     * @param position Позиция данного токена в коде
     * @param invalidCode Код, где содержится неизвестный токен.
     */
    public SyntaxException(String invalidCharacters, int position, String invalidCode) {
        super(String.format("Unknown token \"%s\"", invalidCharacters), position, invalidCode);
    }

}
