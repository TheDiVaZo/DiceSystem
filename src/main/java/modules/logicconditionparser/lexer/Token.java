package modules.logicconditionparser.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Token {

    @Getter
    protected final String sign;

    @Getter
    protected final TokenType tokenType;

    @Getter
    protected final int priority;

    @Override
    public String toString() {
        return "Lexeme{\n" +
                "sign='" + sign + "'\n" +
                "token=" + tokenType + "\n" +
                "priority=" + priority + "\n" +
                "}\n";
    }
}
