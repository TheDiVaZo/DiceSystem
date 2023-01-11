package modules.logicconditionparser.parser;

public enum NodeType {
    BINARY_OPERATION,
    UNARY_OPERATION,
    UNKNOWN_OPERATOR,

    COMPOUND_OPERATOR_START,
    COMPOUND_OPERATOR_END,
    CONDITION_NAME,
    ROOT
}
