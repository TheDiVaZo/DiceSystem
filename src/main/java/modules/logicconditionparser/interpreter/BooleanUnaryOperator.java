package modules.logicconditionparser.interpreter;

import modules.logicconditionparser.lexer.TokenType;

import java.util.function.UnaryOperator;

public interface BooleanUnaryOperator extends EmptyBooleanOperator, UnaryOperator<Boolean> {
    @Override
    default TokenType getTokenType() {
        return TokenType.UNARY_OPERATOR;
    }
}
