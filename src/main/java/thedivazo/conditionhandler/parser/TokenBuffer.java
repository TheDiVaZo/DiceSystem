package thedivazo.conditionhandler.parser;

import thedivazo.conditionhandler.lexer.Token;

import java.util.List;

public class TokenBuffer {

    protected int pos = 0;
    protected List<Token> tokenList;

    public TokenBuffer(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public Token next() {
        return tokenList.get(pos++);
    }

    public Token back() {
        return tokenList.get(pos <= 0 ? 0 : pos--);
    }

    public int getPos() {
        return pos;
    }

    public boolean hasNext() {
        return tokenList.size() > pos+1;
    }
}
