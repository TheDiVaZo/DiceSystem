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
        lexer.putOperator(",", TokenType.DELIMITER);
        lexer.putOperator("[\\+\\-\\*\\/]", TokenType.OPERATOR);
        lexer.putOperator("[0-9]+", TokenType.CONDITION);
        lexer.putOperator("cos", TokenType.FUNCTION);
        lexer.putOperator("sin", TokenType.FUNCTION);
        lexer.putOperator("pow", TokenType.FUNCTION);
        lexer.putOperator("pi", TokenType.CONDITION);
        lexer.putOperator(" +", TokenType.SPACE);
        lexer.putOperator("\\(", TokenType.COMPOUND_START);
        lexer.putOperator("\\)", TokenType.COMPOUND_END);
        String code = "-pow(cos(pi/2),2)-pow(sin(pi/2),2)+cos(0)";
        List<Token> tokenList = lexer.analyze(code);
        Parser parser = new Parser();
        parser.addOperator(new Parser.OperatorData("-", OperatorType.UNARY));
        parser.addOperator(new Parser.OperatorData("*", OperatorType.BINARY),new Parser.OperatorData("/", OperatorType.BINARY));
        parser.addOperator(new Parser.OperatorData("+", OperatorType.BINARY), new Parser.OperatorData("-", OperatorType.BINARY));

        parser.addNumberFunctionArgument("cos",1);
        parser.addNumberFunctionArgument("sin",1);
        parser.addNumberFunctionArgument("pow",2);
        System.out.println(parser.parsing(tokenList));

        Interpreter<Double, Double> interpreter = new Interpreter<>();
        interpreter.setAlternativeConditionConverter(Double::parseDouble);
        interpreter.addCondition("pi", (bol1)->Math.PI);
        interpreter.addBinaryOperator("+", Double::sum);
        interpreter.addBinaryOperator("-", (bol1, bol2)->bol1 - bol2);
        interpreter.addBinaryOperator("/", (bol1, bol2)->bol1 / bol2);
        interpreter.addBinaryOperator("*", (bol1, bol2)->bol1 * bol2);
        interpreter.addUnaryOperator("-", (bol1)->-bol1);
        interpreter.addFunctionOperator("cos", (list)->Math.cos(list.get(0)));
        interpreter.addFunctionOperator("sin", (list)->Math.sin(list.get(0)));
        interpreter.addFunctionOperator("pow", (list)->Math.pow(list.get(0),list.get(1)));
        System.out.println(interpreter.execute(parser.parsing(tokenList), 1d));
    }
}