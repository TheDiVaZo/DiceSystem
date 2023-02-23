package thedivazo.conditionhandler.parser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.collections4.map.LinkedMap;
import thedivazo.conditionhandler.exception.CompileException;
import thedivazo.conditionhandler.exception.ConditionException;
import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.lexer.Lexer;
import thedivazo.conditionhandler.lexer.Token;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.AST.BinaryOperatorNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;
import thedivazo.conditionhandler.parser.AST.OperatorNode;
import thedivazo.conditionhandler.parser.AST.UnaryOperatorNode;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Данный класс предназначен для парсинга списка токенов ({@link Lexer}) в дерево узлов (AST).
 *
 * @author TheDiVaZo
 * @version 2.0
 */
@RequiredArgsConstructor
public class Parser {

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class OperatorData {
        private String signOperator;
        private OperatorType operatorType;
    }


    private final List<Set<OperatorData>> listOfPriorityOperator = SetUniqueList.setUniqueList(new ArrayList<>());


    /**
     * Метод, позволяющий добавить токен(ы) оператора(ов) в список приоритета.
     * Чем позже был добавлен оператор(ы) в список, тем более он(и) приоритетен(ны).
     * @param operatorData Массив с неповторяющимися операторами, которые вы хотите добавить в список приоритета
     * @return возвращает состояние добавления
     */
    public boolean appOperator(OperatorData... operatorData) {
        return listOfPriorityOperator.add(Arrays.stream(operatorData).collect(Collectors.toSet()));
    }

    /**
     * @param tokenList Массив с токенами, который нужно преобразовать в AST дерево
     * @return Возвращает головной узел
     */
    public Node parsing(List<Token> tokenList) {
        return null;
    }






    protected String tokensToCode(List<Token> tokenList) {
        return String.join("",tokenList.stream().map(Token::getSign).toList());
    }
}
