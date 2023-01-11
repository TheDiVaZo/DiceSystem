package modules.logicconditionparser.parser;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import modules.logicconditionparser.lexer.Lexer;
import modules.logicconditionparser.lexer.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    public void parse() throws Exception {
        Logger.init(new JULHandler(java.util.logging.Logger.getGlobal()));
        Lexer lexer = new Lexer();
        lexer.addToken("!", Token.UNARY_OPERATION);
        lexer.addToken("&&", Token.BINARY_OPERATION);
        lexer.addToken("||", Token.BINARY_OPERATION);
        System.out.println(lexer.printPriorityList());
        System.out.println(Parser.parse(lexer.tokenize("!cond1 && cond2 || !cond3")));
    }

}