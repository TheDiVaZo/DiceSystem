package modules.logicconditionparser.parser;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import modules.logicconditionparser.lexer.Lexer;
import modules.logicconditionparser.lexer.TokenType;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

class ParserTest {

    @Test
    void parseTest1() throws Exception {
        Logger.init(new JULHandler(java.util.logging.Logger.getGlobal()));
        Lexer lexer = new Lexer();
        lexer.addToken("||", TokenType.BINARY_OPERATOR);
        lexer.addToken("&&", TokenType.BINARY_OPERATOR);
        lexer.addToken("!", TokenType.UNARY_OPERATOR);
        lexer.addToken("(", TokenType.COMPOUND_OPERATOR_START);
        lexer.addToken(")", TokenType.COMPOUND_OPERATOR_END);
        int i = 0;
        Parser.parseToAST(lexer.tokenize("(((!cond1 && cond2)) || ((cond3 && !cond4)))"));
        System.out.println(i);
        i++;
        Parser.parseToAST(lexer.tokenize("!(!(!(!(!(!(!(!(cond1||cond2)||cond3)||cond4))))))"));
        System.out.println(i);
        i++;
        System.out.println(Parser.parseToAST(lexer.tokenize("cond1 || cond2 || ((((((((cond3))))))))")));
        System.out.println(i);
        i++;
        Parser.parseToAST(lexer.tokenize("!!cond1"));
        System.out.println(i);
        i++;
    }

    @Test
    void parseErrorTest1() throws Exception {
        Logger.init(new JULHandler(java.util.logging.Logger.getGlobal()));
        Lexer lexer = new Lexer();
        lexer.addToken("||", TokenType.BINARY_OPERATOR);
        lexer.addToken("&&", TokenType.BINARY_OPERATOR);
        lexer.addToken("!", TokenType.UNARY_OPERATOR);
        lexer.addToken("(", TokenType.COMPOUND_OPERATOR_START);
        lexer.addToken(")", TokenType.COMPOUND_OPERATOR_END);
        //System.out.println("1 " + Parser.parseToAST(lexer.tokenize("()")));
        //System.out.println("2 " + Parser.parseToAST(lexer.tokenize("(")));
        System.out.println("3 " + Parser.parseToAST(lexer.tokenize("||")));
        System.out.println("4 " + Parser.parseToAST(lexer.tokenize("cond ||")));
        System.out.println("5 " + Parser.parseToAST(lexer.tokenize("&&")));
        System.out.println("6 " + Parser.parseToAST(lexer.tokenize("(||)")));

        /*
        (
        )

         */
    }

}