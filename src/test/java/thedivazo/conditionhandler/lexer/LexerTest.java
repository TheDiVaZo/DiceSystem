package thedivazo.conditionhandler.lexer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void analyze() throws Exception {
        Lexer lexer = new Lexer();
        lexer.putOperator(Pattern.quote("&&"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("||"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATION);
        lexer.putOperator("[0-9a-zA-Z_\\-]+", TokenType.CONDITION_GROUP);
        lexer.putOperator("[\t,\s,\n]+", TokenType.SPACE);

        System.out.println(Arrays.toString(new List[]{lexer.analyze("cond1 || %% cond2 && cond3 ")}));;
    }
}