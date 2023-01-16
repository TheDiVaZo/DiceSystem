package modules.logicconditionparser.interpreter;

import modules.logicconditionparser.exception.IllegalArgumentNumberException;
import modules.logicconditionparser.exception.IllegalConditionException;
import modules.logicconditionparser.exception.IllegalTokenException;
import modules.logicconditionparser.exception.UnknownOperatorException;
import modules.logicconditionparser.lexer.Lexer;
import modules.logicconditionparser.lexer.TokenType;
import modules.logicconditionparser.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    @Test
    void execute() throws UnknownOperatorException, IllegalArgumentNumberException, IllegalConditionException, IllegalTokenException {
        Lexer lexer = new Lexer();
        lexer.addToken("||", TokenType.BINARY_OPERATOR);
        lexer.addToken("&&", TokenType.BINARY_OPERATOR);
        lexer.addToken("!", TokenType.UNARY_OPERATOR);
        lexer.addToken("(", TokenType.COMPOUND_OPERATOR_START);
        lexer.addToken(")", TokenType.COMPOUND_OPERATOR_END);

        Interpreter interpreter = new Interpreter();
        interpreter.addCondition("cond1", ()->false);
        interpreter.addCondition("cond2", ()->false);
        interpreter.addCondition("cond3", ()->false);
        interpreter.addCondition("cond4", ()->false);

        interpreter.addOperator("||", (Interpreter.CallbackBinaryOperator) (cond1, cond2) -> cond1 || cond2);
        interpreter.addOperator("&&", (Interpreter.CallbackBinaryOperator) (cond1, cond2) -> cond1 && cond2);
        interpreter.addOperator("!", (Interpreter.CallbackUnaryOperator) (cond1) -> !cond1);

        System.out.println(interpreter.execute(Parser.parseToAST(lexer.tokenize("(cond1 || cond2) || (cond3 || cond4)"))));
    }
}