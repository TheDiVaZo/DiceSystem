package thedivazo.conditionhandler.parser;

import org.junit.jupiter.api.Test;
import thedivazo.conditionhandler.exception.CompileException;
import thedivazo.conditionhandler.exception.ConditionException;
import thedivazo.conditionhandler.exception.FanoConditionException;
import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.lexer.Lexer;
import thedivazo.conditionhandler.lexer.TokenType;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    static Lexer lexer = new Lexer();

    {
        lexer.putOperator(Pattern.quote("&&"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("||"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATOR);
        lexer.putOperator("\\*", TokenType.UNARY_OPERATOR);
        lexer.putOperator(Pattern.quote("("), TokenType.COMPOUND_START);
        lexer.putOperator(Pattern.quote(")"), TokenType.COMPOUND_END);
        //lexer.putOperator(Pattern.quote("|"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATOR);
        lexer.putOperator("[0-9a-zA-Z_\\-]+", TokenType.CONDITION);
        lexer.putOperator("[\t,\s,\n]+", TokenType.SPACE);
    }

    @Test
    void parserTest1() throws CompileException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("!!cond1 && cond5 || cond2"));
        assertEquals("||(&&(!(!(cond1)),cond5),cond2)", node.toString());
    }
    @Test
    void parserTest2() throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("cond1"));
        assertEquals("cond1", node.toString());
        //System.out.println(node);
    }
    @Test
    void parserTest3() throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("((((!!!!cond1))))"));
        assertEquals("!(!(!(!(cond1))))", node.toString());
        //System.out.println(node);
    }

    @Test
    void parserTest4() throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("(!cond1 || cond2) && cond3"));
        assertEquals("&&(||(!(cond1),cond2),cond3)", node.toString());
        //System.out.println(node);
    }

    @Test
    void parserTest5() throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("!cond1 || cond2 && cond3"));
        assertEquals("||(!(cond1),&&(cond2,cond3))", node.toString());
        //System.out.println(node);
    }

    @Test
    void parserTest6()throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("cond1 || cond2 || cond3 || cond4 || cond5 || cond6"));
        assertEquals("||(||(||(||(||(cond1,cond2),cond3),cond4),cond5),cond6)", node.toString());
        //System.out.println(node);
    }

    @Test
    void parserTest7()throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("cond1 && cond2 || cond3 && cond4"));
        assertEquals("||(&&(cond1,cond2),&&(cond3,cond4))", node.toString());
        System.out.println(node);
    }

    @Test
    void parserTest8()throws Exception {
        Parser parser = new Parser();
        lexer.putOperator("\\*", TokenType.UNARY_OPERATOR);
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("*", OperatorType.UNARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("*cond1 && cond2"));
        assertEquals("*(&&(cond1,cond2))", node.toString());
        System.out.println(node);
    }

    @Test
    void parserTest9()throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("*", OperatorType.UNARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        Node node = parser.parsing(lexer.analyze("(*cond1) && cond2"));
        assertEquals("&&(*(cond1),cond2)", node.toString());
        System.out.println(node);
    }

    @Test
    void parserTest10()throws Exception {
        Parser parser = new Parser();
        parser.addPriorityOperator("!", OperatorType.UNARY);
        parser.addPriorityOperator("&&", OperatorType.BINARY);
        parser.addPriorityOperator("||", OperatorType.BINARY);
        parser.addPriorityOperator("*", OperatorType.UNARY);
        Node node = parser.parsing(lexer.analyze("!*!cond1 || cond2"));
        assertEquals("!(*(||(!(cond1),cond2)))", node.toString());
        //System.out.println(node);
    }
}