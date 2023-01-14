package modules.logicconditionparser.parser;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import modules.logicconditionparser.lexer.Lexer;
import modules.logicconditionparser.lexer.TokenType;
import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void parse() throws Exception {
        Logger.init(new JULHandler(java.util.logging.Logger.getGlobal()));
        Lexer lexer = new Lexer();
        lexer.addToken("||", TokenType.BINARY_OPERATOR);
        lexer.addToken("&&", TokenType.BINARY_OPERATOR);
        lexer.addToken("!", TokenType.UNARY_OPERATOR);
        lexer.addToken("(", TokenType.COMPOUND_OPERATOR_START);
        lexer.addToken(")", TokenType.COMPOUND_OPERATOR_START);
        System.out.println(Parser.parseToAST(lexer.tokenize("!cond1 && (cond2 || !cond3)")));
    }

}