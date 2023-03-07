package thedivazo.parserexpression.parser;

import org.junit.jupiter.api.Test;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.lexer.Lexer;
import thedivazo.parserexpression.lexer.Token;
import thedivazo.parserexpression.lexer.TokenType;

import java.util.Arrays;
import java.util.List;

class ParserTest {

    @Test
    void parsingBoolean() throws CompileException {
        Lexer lexer = new Lexer();
        lexer.putOperator("\\!", TokenType.OPERATOR);
        lexer.putOperator("\\|\\|", TokenType.OPERATOR);
        lexer.putOperator("\\&\\&", TokenType.OPERATOR);
        lexer.putOperator("[a-zA-Z0-9]+", TokenType.CONDITION);
        lexer.putOperator(" +", TokenType.SPACE);
        lexer.putOperator("\\(", TokenType.COMPOUND_START);
        lexer.putOperator("\\)", TokenType.COMPOUND_END);
        lexer.putOperator("\\:", TokenType.OPERATOR);
        lexer.putOperator("\\?",TokenType.OPERATOR);
        String code = "(cond3 && !cond4 || cond5) && cond6 ? true:false";
        List<Token> tokenList = lexer.analyze(code);
        Parser parser = new Parser();
        parser.addOperator(new Parser.OperatorData("!", OperatorType.UNARY));
        parser.addOperator(new Parser.OperatorData("&&", OperatorType.BINARY));
        parser.addOperator(new Parser.OperatorData("||", OperatorType.BINARY));
        parser.addOperator(new Parser.OperatorData("?", OperatorType.TERNARY_1), new Parser.OperatorData(":", OperatorType.TERNARY_2));
        System.out.println(parser.parsing(tokenList));
    }

    @Test
    void parsingArithmeticTest() throws CompileException {
        Lexer lexer = new Lexer();
        lexer.putOperator("[\\+\\-\\*\\/]", TokenType.OPERATOR);
        lexer.putOperator("[0-9]+", TokenType.CONDITION);
        lexer.putOperator(" +", TokenType.SPACE);
        lexer.putOperator("\\(", TokenType.COMPOUND_START);
        lexer.putOperator("\\)", TokenType.COMPOUND_END);
        lexer.putOperator("\\.", TokenType.METHOD_REFERENCE);
        lexer.putOperator("get", TokenType.METHOD);
        lexer.putOperator("go", TokenType.METHOD);
        String code = "-2+6*2-4/(-2).get(1).go()"; // =12
        List<Token> tokenList = lexer.analyze(code);
        System.out.println(Arrays.toString(tokenList.toArray()));
        Parser parser = new Parser();
        parser.addOperator(new Parser.OperatorData("-", OperatorType.UNARY));
        parser.addOperator(new Parser.OperatorData("*", OperatorType.BINARY),new Parser.OperatorData("/", OperatorType.BINARY));
        parser.addOperator(new Parser.OperatorData("+", OperatorType.BINARY), new Parser.OperatorData("-", OperatorType.BINARY));
        System.out.println(parser.parsing(tokenList));
    }

    @Test
    void parsingFunctioncTest() throws CompileException {
        Lexer lexer = new Lexer();
        lexer.putOperator("[a-zA-Z]+", TokenType.FUNCTION);
        lexer.putOperator("[0-9]+", TokenType.CONDITION);
        lexer.putOperator("\\,", TokenType.DELIMITER);
        lexer.putOperator("\\(", TokenType.COMPOUND_START);
        lexer.putOperator("\\)", TokenType.COMPOUND_END);
        lexer.putOperator(" +", TokenType.SPACE);
        String code = "gg( aza(1,2))"; // =12
        List<Token> tokenList = lexer.analyze(code);
        System.out.println(Arrays.toString(tokenList.toArray()));
        Parser parser = new Parser();
        System.out.println(parser.parsing(tokenList));
    }
}