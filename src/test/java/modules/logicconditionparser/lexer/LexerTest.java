package modules.logicconditionparser.lexer;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void tokenize1() {
        Logger.init(new JULHandler(java.util.logging.Logger.getGlobal()));
        Lexer lexer = new Lexer();
        lexer.addToken("||", Token.BINARY_OPERATION);
        lexer.addToken("&&", Token.BINARY_OPERATION);
        lexer.addToken("!", Token.UNARY_OPERATION);

        String code = "name1|| name2 && !name3 && name4 && name5";
        String fS = "";
        for (Lexeme lexeme : lexer.tokenize(code)) {
            Logger.info(1);
            fS += "\n"+(lexeme.token.name() + ": " + lexeme.sign);
        }
        Logger.info(fS);
    }
}