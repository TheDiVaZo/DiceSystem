package thedivazo.conditionhandler.lexer;

import java.util.regex.Pattern;

public enum TokenType {
    CONDITION,
    BINARY_OPERATION,
    UNARY_OPERATION,
    COMPOUND_START,
    COMPOUND_END,
    TRUE,
    FALSE,
    SPACE;
}
