package thedivazo.conditionhandler.lexer;

import lombok.NoArgsConstructor;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Лексер, принимающий исходный код, и выдающий токены
 * @author TheDiVaZo
 *
 */
@NoArgsConstructor


public class Lexer {


    /**
     * Хранит RegEx'ы и типы токенов, которые будут строиться по этим RegEx'ам.
     * В каком порядке и добавили, в таком порядке и будет вестись их поиск.
     * RegEx'ы для переменных нужно добавлять в самом конце.
     */
    protected Map<String, TokenType> operatorsSet = new LinkedMap<>();


    /**
     * @param sign regEx, по которому будет присваиваться тип токена
     * @param tokenType Тип токена
     */
    public void putOperator(String sign, TokenType tokenType) {
        operatorsSet.put(sign, tokenType);
    }

    /**
     * @param sign regEx, по которому будет удаляться тип токена
     * @return тип удаленного токена
     */
    public TokenType removeOperator(String sign) {
        return operatorsSet.remove(sign);
    }


    /**
     * Метод анализирует код и строит токены по нему.
     * @param code исходный код. Пример: cond1 || cond2 !(cond3 && cond4)
     * @return Возвращает массив с токенами.
     * @throws SyntaxException если в коде присутствуют синтаксические ошибки, то будет вызвано это исключение;
     */
    public List<Token> analyze(String code) throws SyntaxException {
        List<Token> result = new ArrayList<>();
        StringBuilder sliceCode = new StringBuilder(code);
        int position = 0;
        while(true) {
            if(sliceCode.isEmpty()) return result;
            String token = null;
            TokenType tokenType = null;
            for (String regEx : operatorsSet.keySet()) {
                Matcher matcher = Pattern.compile("^"+regEx).matcher(sliceCode);
                if(matcher.find()) {
                    token = matcher.group();
                    tokenType = operatorsSet.getOrDefault(regEx, null);
                    break;
                }
            }
            if(Objects.isNull(token) || Objects.isNull(tokenType)) {
                throw new SyntaxException(position, code);
            }
            result.add(new Token(tokenType, token, position));
            position += token.length();
            sliceCode.replace(0, token.length(), "");
        }
    }



}
