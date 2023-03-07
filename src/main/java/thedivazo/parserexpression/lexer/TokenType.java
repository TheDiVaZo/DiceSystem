package thedivazo.parserexpression.lexer;

public enum TokenType {
    EOF,
    CONDITION,

    OPERATOR,

    FUNCTION,
    COMPOUND_START,
    COMPOUND_END,
    SPACE,

    DELIMITER,

    METHOD,

    METHOD_REFERENCE, START_VARIABLE_SYMBOL, LOCAL_VARIABLE;
}
