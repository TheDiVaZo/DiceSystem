package modules.logicconditionparser.lexer;


import org.apache.commons.collections4.map.LinkedMap;
import thedivazo.utils.MatcherWrapper;

import java.util.*;
import java.util.regex.Pattern;

public class Lexer {
    protected static final String CONDITION_NAME = "a-zA-Z0-9_";

    private LinkedMap<String, TokenType> tokens = new LinkedMap<>();

    public void addToken(String sign, TokenType tokenType) {
        tokens.put(sign, tokenType);
    }
    public void removeToken(String sign) {
        tokens.remove(sign);
    }

    public List<Token> tokenize(String code) {
        MatcherWrapper matcherWrapper = new MatcherWrapper(getPatternForTokens());
        return matcherWrapper.matchAll(code).stream().map(this::toLexeme).toList();
    }

    public Pattern getPatternForTokens() {
        return Pattern.compile("("+ String.join("|", tokens.keySet().stream().map(Pattern::quote).toList()) +")|(["+CONDITION_NAME+"]+)");
    }

    public Token toLexeme(String operator) {
        return new Token(operator, tokens.getOrDefault(operator, TokenType.CONDITION_NAME), tokens.indexOf(operator));
    }
}
