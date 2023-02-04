package thedivazo.conditionhandler.parser;

import org.junit.jupiter.api.Test;
import thedivazo.conditionhandler.exception.FanoConditionException;
import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.exception.UnsupportedOperatorException;
import thedivazo.conditionhandler.lexer.Lexer;
import thedivazo.conditionhandler.lexer.TokenType;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void generateCodeFromToken() {
    }

    @Test
    void generateRules() {
    }

    @Test
    void parse() throws SyntaxException, FanoConditionException, UnsupportedOperatorException {
        Lexer lexer = new Lexer();
        lexer.putOperator(Pattern.quote("&&"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("||"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATION);
        lexer.putOperator(Pattern.quote("("), TokenType.COMPOUND_START);
        lexer.putOperator(Pattern.quote(")"), TokenType.COMPOUND_END);
        //lexer.putOperator(Pattern.quote("|"), TokenType.BINARY_OPERATION);
        //lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATION);
        lexer.putOperator("[0-9a-zA-Z_\\-]+", TokenType.CONDITION);
        lexer.putOperator("[\t,\s,\n]+", TokenType.SPACE);
        Parser parser = new Parser();
        parser.generateRules("&&", "||");
        System.out.println(parser.parse(lexer.analyze("cond1 || cond2 && cond3")));

        /*
Unknown cd token: ||
cond1 || ||
          ^
         */
    }
}