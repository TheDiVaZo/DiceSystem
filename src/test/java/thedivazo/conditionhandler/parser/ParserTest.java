package thedivazo.conditionhandler.parser;

import org.junit.jupiter.api.Test;
import thedivazo.conditionhandler.exception.FanoConditionException;
import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.lexer.Lexer;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.AST.OperatorNode;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

class ParserTest {

    @Test
    void generateCodeFromToken() {
    }

    @Test
    void generateRules() {
    }

    @Test
    void parse() throws SyntaxException, FanoConditionException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Lexer lexer = new Lexer();
        lexer.putOperator(Pattern.quote("&&"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("||"), TokenType.BINARY_OPERATOR);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATOR);
        lexer.putOperator(Pattern.quote("("), TokenType.COMPOUND_START);
        lexer.putOperator(Pattern.quote(")"), TokenType.COMPOUND_END);
        //lexer.putOperator(Pattern.quote("|"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATOR);
        lexer.putOperator("[0-9a-zA-Z_\\-]+", TokenType.CONDITION);
        lexer.putOperator("[\t,\s,\n]+", TokenType.SPACE);
        Parser parser = new Parser();
        parser.specialOperators.put("||", TestOperatorClass.class);
        Node node = parser.parsing(lexer.analyze("(!cond1) || (!cond2)"));
        System.out.println(node);
    }
}