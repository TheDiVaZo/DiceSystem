package thedivazo.conditionhandler.lexer;

import thedivazo.conditionhandler.exception.ConditionException;

public class SyntaxException extends ConditionException {


    /**
     * @param position Позиция неправильного символа.
     */
    public SyntaxException(int position) {
        super("Unknown character in "+position+" position.");
    }


    /**
     * @param position Позиция неправильного символа
     * @param invalidCode Код, где этот символ находится.
     */
    public SyntaxException(int position, String invalidCode) {
        //TODO: сделать более красиво
        super("Unknown character in "+position+" position.\n"+
                invalidCode+"\n"+String.format("%"+position+"s","")+"^");
    }
}
