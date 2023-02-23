package thedivazo.conditionhandler.parser;

import lombok.RequiredArgsConstructor;
import thedivazo.conditionhandler.lexer.Token;

import java.util.List;

@RequiredArgsConstructor
public class TokenBuffer {
    protected final List<Token> tokenList;

    protected int currentIndex = 0;

    public Token next() {
        return tokenList.get(currentIndex++);
    }

    public void prev() {
        currentIndex--;
    }

    public boolean hasNext() {
        return tokenList.size() > currentIndex;
    }
}
