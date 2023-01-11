package modules.logicconditionparser.lexer;

import thedivazo.utils.MatcherWrapper;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Lexer {

    private Map<String, Token> tokens = new LinkedHashMap<>();

    public void addToken(String sign, Token token) {
        tokens.put(sign, token);
    }
    public void removeToken(String sign) {
        tokens.remove(sign);
    }

    public String printPriorityList() {
        String result = "";
        int i = 0;
        for (String s : tokens.keySet()) {
            result += s + ": "+i + "\n";
            i++;
        }
        return result;
    }

    public List<Lexeme> tokenize(String code) {
        List<Lexeme> lexemeList = new ArrayList<>();
        code = code.trim();
        String lexemeRegEx = String.join("|",tokens.keySet().stream().map(Pattern::quote).collect(Collectors.toSet()));
        MatcherWrapper patternLexeme = new MatcherWrapper(Pattern.compile("("+lexemeRegEx+")"));
        String[] signs = patternLexeme.matchAll(code).toArray(new String[]{});
        String[] remains = code.split(patternLexeme.pattern());
        for (int i = 0; i < signs.length + remains.length; i++) {
            if((i % 2 == 0) && i/2 < remains.length) {
                String remainder = remains[i/2].trim();
                if(remainder.length()!=0) {
                    lexemeList.add(new Lexeme(remainder, Token.CONDITION_NAME, 0));
                }
            }
            else if((i % 2 != 0) && i/2 < signs.length) {
                String sign = signs[i/2].trim() ;
                Set<Map.Entry<String, Token>> tokenSet = tokens.entrySet();
                int priority = 0;
                for (Map.Entry<String, Token> stringTokenEntry : tokenSet) {
                    if(stringTokenEntry.getKey().equals(sign)) {
                        lexemeList.add(new Lexeme(sign, stringTokenEntry.getValue(), priority));
                        break;
                    }
                    else priority++;
                }
            }
        }
        return lexemeList;
    }
}
