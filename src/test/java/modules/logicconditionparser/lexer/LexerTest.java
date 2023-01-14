package modules.logicconditionparser.lexer;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import org.junit.jupiter.api.Test;

class LexerTest {

    @Test
    void tokenize() {
        Logger.init(new JULHandler(java.util.logging.Logger.getGlobal()));
        Lexer lexer = new Lexer();
        lexer.addToken("||", TokenType.BINARY_OPERATOR);
        lexer.addToken("&&", TokenType.BINARY_OPERATOR);
        lexer.addToken("!", TokenType.UNARY_OPERATOR);

        String code = "(name1|| (!name2)) && !name3 && name4 && name5";
        String fS = "";
        for (Token token : lexer.tokenize(code)) {
            Logger.info(token);
        }
    }
}