package thedivazo.conditionhandler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import thedivazo.conditionhandler.lexer.Token;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class TokenBuffer {
    protected final List<Token> tokenList;

    @Getter
    protected int pos = 0;

    public boolean hasNext() {
        return tokenList.size() > (pos+1);
    }

    public boolean hasPrev() {
        return (pos-1) >= 0;
    }

    public Token next() {
        if(hasNext()) return tokenList.get(pos++);
        else return null;
    }

    public Token prev() {
        if(hasPrev()) return tokenList.get(pos--);
        else return null;
    }

    public List<Token> getTokenList() {
        return Collections.unmodifiableList(tokenList);
    }

    public Token current() {
        return tokenList.get(pos);
    }
}
