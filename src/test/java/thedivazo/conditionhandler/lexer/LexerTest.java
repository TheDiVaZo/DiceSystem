package thedivazo.conditionhandler.lexer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void analyze() throws Exception {
        Lexer lexer = new Lexer();
        lexer.putOperator(Pattern.quote("&&"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("||"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("|"), TokenType.BINARY_OPERATION);
        lexer.putOperator(Pattern.quote("!"), TokenType.UNARY_OPERATION);
        lexer.putOperator("[0-9a-zA-Z_\\-]+", TokenType.CONDITION);
        lexer.putOperator("[\t,\s,\n]+", TokenType.SPACE);
        System.out.println(Arrays.toString(new List[]{lexer.analyze("cond1 || %% cond2 && cond3 ")}));;
    }

    @Test
    void ddd() {
        Matcher matcher = Pattern.compile("([a-z]+):([a-z]+)").matcher("f gggg:dddd");
        while (matcher.find()) {
            System.out.println(matcher.groupCount());
            System.out.println(matcher.group());
        }
    }
}