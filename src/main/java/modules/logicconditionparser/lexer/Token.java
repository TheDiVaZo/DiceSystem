package modules.logicconditionparser.lexer;

public enum Token {
    BINARY_OPERATION,
    UNARY_OPERATION,
    UNKNOWN_OPERATOR,

    COMPOUND_OPERATOR_START,
    COMPOUND_OPERATOR_END,

    CONDITION_NAME,
}
