package thedivazo.conditionhandler.interpreter;

import org.junit.jupiter.api.Test;
import thedivazo.conditionhandler.exception.CompileException;
import thedivazo.conditionhandler.exception.InterpreterException;
import thedivazo.conditionhandler.lexer.Lexer;
import thedivazo.conditionhandler.lexer.Token;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.OperatorType;
import thedivazo.conditionhandler.parser.Parser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    @Test
    void executingBoolean() throws CompileException, InterpreterException {
        Lexer lexer = new Lexer();
        lexer.putOperator("\\!", TokenType.OPERATOR);
        lexer.putOperator("\\|\\|", TokenType.OPERATOR);
        lexer.putOperator("\\&\\&", TokenType.OPERATOR);
        lexer.putOperator("[a-zA-Z0-9]+", TokenType.CONDITION);
        lexer.putOperator(" +", TokenType.SPACE);
        lexer.putOperator("\\(", TokenType.COMPOUND_START);
        lexer.putOperator("\\)", TokenType.COMPOUND_END);
        String code = "!cond1 && cond2 || cond3 || cond4";
        List<Token> tokenList = lexer.analyze(code);
        Parser parser = new Parser();
        parser.addOperator(new Parser.OperatorData("!", OperatorType.UNARY));
        parser.addOperator(new Parser.OperatorData("&&", OperatorType.BINARY));
        parser.addOperator(new Parser.OperatorData("||", OperatorType.BINARY));
        //System.out.println(parser.parsing(tokenList));

        Interpreter<Boolean, Boolean> interpreter = new Interpreter<>();
        interpreter.addCondition("cond1", (bol1)->true);
        interpreter.addCondition("cond2", (bol1)->true);
        interpreter.addCondition("cond3", (bol1)->false);
        interpreter.addCondition("cond4", (bol1)->true);
        interpreter.addBinaryOperator("&&", (bol1, bol2)->bol1 && bol2);
        interpreter.addBinaryOperator("||", (bol1, bol2)->bol1 || bol2);
        interpreter.addUnaryOperator("!", (bol1)->!bol1);
        System.out.println(interpreter.execute(parser.parsing(tokenList), true));
    }

    @Test
    void executingArithmeticTest() throws CompileException, InterpreterException {
        Lexer lexer = new Lexer();
        lexer.putOperator("[\\+\\-\\*\\/]", TokenType.OPERATOR);
        lexer.putOperator("[0-9]+", TokenType.CONDITION);
        lexer.putOperator(" +", TokenType.SPACE);
        lexer.putOperator("\\(", TokenType.COMPOUND_START);
        lexer.putOperator("\\)", TokenType.COMPOUND_END);
        String code = "-2+6*2-4/(-2)"; // =12
        List<Token> tokenList = lexer.analyze(code);
        Parser parser = new Parser();
        parser.addOperator(new Parser.OperatorData("-", OperatorType.UNARY));
        parser.addOperator(new Parser.OperatorData("*", OperatorType.BINARY),new Parser.OperatorData("/", OperatorType.BINARY));
        parser.addOperator(new Parser.OperatorData("+", OperatorType.BINARY), new Parser.OperatorData("-", OperatorType.BINARY));
        System.out.println(parser.parsing(tokenList));

        Interpreter<Integer, Integer> interpreter = new Interpreter<>();
        interpreter.addCondition("1", (bol1)->1);
        interpreter.addCondition("2", (bol1)->2);
        interpreter.addCondition("6", (bol1)->6);
        interpreter.addCondition("4", (bol1)->4);
        interpreter.addBinaryOperator("+", Integer::sum);
        interpreter.addBinaryOperator("-", (bol1, bol2)->bol1 - bol2);
        interpreter.addBinaryOperator("/", (bol1, bol2)->bol1 / bol2);
        interpreter.addBinaryOperator("*", (bol1, bol2)->bol1 * bol2);
        interpreter.addUnaryOperator("-", (bol1)->-bol1);
        System.out.println(interpreter.execute(parser.parsing(tokenList), 1));
    }
}