package thedivazo.conditionhandler.parser;

import thedivazo.conditionhandler.exception.SyntaxException;

public interface FunctionParser<T, R> {
    R applyAndException(T t) throws SyntaxException;
}
