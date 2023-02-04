package thedivazo.conditionhandler.parser;

import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.exception.UnknownOperatorException;

public interface FunctionParser<T, R> {
    R applyAndException(T t) throws SyntaxException;
}
