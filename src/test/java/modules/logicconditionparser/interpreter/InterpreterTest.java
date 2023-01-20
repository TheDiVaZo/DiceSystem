package modules.logicconditionparser.interpreter;

import modules.logicconditionparser.exception.*;
import modules.logicconditionparser.lexer.Lexer;
import modules.logicconditionparser.lexer.TokenType;
import modules.logicconditionparser.parser.Parser;
import org.junit.jupiter.api.Test;

class InterpreterTest {

    @Test
    void execute() throws ExecuteException, ParsingException {
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

        interpreter.addOperator("||", (BooleanBinaryOperator) (cond1, cond2) -> cond1 || cond2);
        interpreter.addOperator("&&", (BooleanBinaryOperator) (cond1, cond2) -> cond1 && cond2);
        interpreter.addOperator("!", (BooleanUnaryOperator) aBoolean -> !aBoolean);

        System.out.println(interpreter.execute(Parser.parseToAST(lexer.tokenize("(()"))));
    }
}