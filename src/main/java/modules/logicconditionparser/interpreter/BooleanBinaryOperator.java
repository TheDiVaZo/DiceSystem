package modules.logicconditionparser.interpreter;

import modules.logicconditionparser.lexer.TokenType;

import java.util.function.BinaryOperator;

public interface BooleanBinaryOperator extends EmptyBooleanOperator, BinaryOperator<Boolean> {
    @Override
    default TokenType getTokenType() {
        return TokenType.BINARY_OPERATOR;
    }

}
