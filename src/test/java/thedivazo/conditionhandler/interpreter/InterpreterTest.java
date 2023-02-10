package thedivazo.conditionhandler.interpreter;

import org.junit.jupiter.api.Test;
import thedivazo.conditionhandler.exception.CompileException;
import thedivazo.conditionhandler.exception.FanoConditionException;
import thedivazo.conditionhandler.exception.InterpreterException;
import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.lexer.Lexer;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.Node;
import thedivazo.conditionhandler.parser.OperatorType;
import thedivazo.conditionhandler.parser.Parser;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    @Test
    void interpreterBooleanTest() throws CompileException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterpreterException {
        Lexer lexer = new Lexer();
        lexer.putOperator(Pattern.quote("&&"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("||"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATOR);
        lexer.putOperator(Pattern.quote("("), TokenType.COMPOUND_START);
        lexer.putOperator(Pattern.quote(")"), TokenType.COMPOUND_END);
        lexer.putOperator("[0-9a-zA-Z_\\-]+", TokenType.CONDITION);
        lexer.putOperator("[\t,\s,\n]+", TokenType.SPACE);

        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node mainNode = parser.parsing(lexer.analyze("cond1 && cond5 || cond2"));

        Interpreter<Boolean, Boolean> interpreter = new Interpreter<>();
        interpreter.addBinaryOperator("&&", (bol1, bol2)->bol1 && bol2);
        interpreter.addBinaryOperator("||", (bol1, bol2)->bol1 || bol2);
        interpreter.addUnaryOperator("!", bol1->!bol1);

        interpreter.addCondition("cond1", aBoolean -> false);
        interpreter.addCondition("cond5", aBoolean -> true);
        interpreter.addCondition("cond2", aBoolean -> false);

        System.out.println(interpreter.execute(mainNode, true));


    }

    @Test
    void interpreterFigureTest() throws CompileException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterpreterException {
        Lexer lexer = new Lexer();
        lexer.putOperator(Pattern.quote("+"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("-"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("*"), TokenType.BINARY_OPERATOR);
        //lexer.putOperator(Pattern.quote("-"), TokenType.UNARY_OPERATOR);
        lexer.putOperator(Pattern.quote("("), TokenType.COMPOUND_START);
        lexer.putOperator(Pattern.quote(")"), TokenType.COMPOUND_END);
        lexer.putOperator("[0-9]+", TokenType.CONDITION);
        lexer.putOperator("[\t,\s,\n]+", TokenType.SPACE);

        Parser parser = new Parser();
        //parser.addPriorityOperator("-", OperatorType.UNARY);
        parser.addPriorityOperator("*", OperatorType.BINARY);
        parser.addPriorityOperator("+", OperatorType.BINARY);
        parser.addPriorityOperator("-", OperatorType.BINARY);
        Node mainNode = parser.parsing(lexer.analyze("2 + 3 * (3-5) + 5 * 2"));

        Interpreter<Double, Double> interpreter = new Interpreter<>();
        interpreter.addBinaryOperator("+", Double::sum);
        interpreter.addBinaryOperator("-", (bol1, bol2)->bol1 - bol2);
        interpreter.addBinaryOperator("*", (bol1, bol2)->bol1 * bol2);
        //interpreter.addUnaryOperator("-", bol1->-bol1);

        interpreter.addCondition("2", aFigure -> 2d);
        interpreter.addCondition("3", aFigure -> 3d);
        interpreter.addCondition("5", aFigure -> 5d);

        System.out.println(interpreter.execute(mainNode, 0d));


    }

}