package thedivazo.conditionhandler.parser;

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

/**
 * Данный класс предназначен для парсинга списка токенов ({@link Lexer}) в дерево узлов (AST).
 *
 * RULES PARSING
 * EXPR -> OPERATOR_2* EOF;
 * OPERATOR_2 -> ( 'U_OP'? OPERATOR_1 )|( OPERATOR_1 ('B_OP' OPERATOR_1 )* );
 * OPERATOR_1 -> ( 'U_OP'? VARIABLE )|( VARIABLE ('B_OP' VARIABLE)* );
 * VARIABLE -> CONDITION | 'compoundStart' EXPR 'compoundEnd';
 * @author TheDiVaZo
 * @version 1.0
 */
@RequiredArgsConstructor
public class Parser {


    /**
     * Здесь хранятся токены операторов и их типов. Порядок добавления элемента влияет на приоритет оператора.
     * Первый добавленный элемент будет самым приоритетным.
     */
    protected LinkedMap<String, OperatorType> priorityOperators = new LinkedMap<>();


    public void addPriorityOperator(String sign, OperatorType operatorType) {
        priorityOperators.put(sign, operatorType);
    }


    /**
     * @return Возвращает неизменяемый LinkedMap, содержащий операторы, отсортированные в порядке их приоритета (от самого высокого, до самого низкого приоритета)
     */
    public Map<String, OperatorType> getPriorityOperators() {
        return Collections.unmodifiableMap(priorityOperators);
    }

    /**
     * Если оператор является нестандартным, то можно написать для него свою реализацию
     */
    protected Map<String, Class<? extends OperatorNode >> specialOperators = new HashMap<>();

    /**
     * @param tokenList буфер с токенами
     * @return Возвращает коренной узел, который является вершиной AST дерева.
     */
    public Node parsing(List<Token> tokenList) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompileException {
        TokenBuffer tokenBuffer = new TokenBuffer(tokenList);
        Node mainNode = exprParsing(tokenBuffer);
        Token endToken;
        do {
            endToken = tokenBuffer.next();
        }
        while (endToken.getLexemeType() == TokenType.SPACE);
        if(endToken.getLexemeType() != TokenType.EOF){
            throw new SyntaxException(String.format("Excess \"%s\"",endToken.getSign()), endToken.getPosition(), tokensToCode(tokenList));
        }
        else return mainNode;
    }

    /**
     * @param tokenBuffer буфер с токенами
     * @return Возвращает главный узел из данного набора токенов.
     */
    protected Node exprParsing(TokenBuffer tokenBuffer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompileException {
        Token token = tokenBuffer.next();
        if (token.getLexemeType() == TokenType.EOF) {
            throw new CompileException("There can be no empty expression");
        } else {
            tokenBuffer.prev();
            return priorityOperator(priorityOperators.size()-1,tokenBuffer);

        }
    }


    /**
     * @param priority приоритет оператора, которому нужно передать буффер
     * @param tokenBuffer буфер с токенами.
     * @return Возвращает значение, возвращаемое оператором. Если приоритет ниже нуля, то возвращает значение данного метода: {@link Parser#variableParsing(TokenBuffer)}
     */
    protected Node priorityOperator(int priority, TokenBuffer tokenBuffer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompileException {
        if(priority < 0) return variableParsing(tokenBuffer);
        else {
            String operatorSign = priorityOperators.get(priority);
            OperatorType operatorType = priorityOperators.get(operatorSign);
            switch (operatorType) {
                case UNARY -> {
                    return unaryParsing(tokenBuffer, operatorSign);
                }
                case BINARY -> {
                    return binaryParsing(tokenBuffer, operatorSign);
                }
                default -> {
                    return null;
                }
            }
        }
    }

    /**
     * @param tokenBuffer буфер с токенами
     * @return Возвращает узел, который является бинарным оператором, либо оператором более низкого приоритета.
     */
    protected Node binaryParsing(TokenBuffer tokenBuffer, String sign) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, CompileException {

        Node variable = priorityOperator(priorityOperators.indexOf(sign)-1,tokenBuffer);
        while (tokenBuffer.hasNext()) {
            Token token = tokenBuffer.next();
            switch (token.getLexemeType()) {
                case SPACE -> {}
                case BINARY_OPERATOR -> {
                    if(!token.getSign().equals(sign)) {
                        tokenBuffer.prev();
                        return variable;
                    }
                    BinaryOperatorNode binaryOperatorNode;
                    if(specialOperators.containsKey(token.getSign())) {
                        binaryOperatorNode = (BinaryOperatorNode) specialOperators.get(token.getSign()).getConstructor(String.class).newInstance(token.getSign());
                    }
                    else binaryOperatorNode = new BinaryOperatorNode(token.getSign());
                    Set<Node> childrenNodes = new LinkedHashSet<>();
                    childrenNodes.add(variable);
                    childrenNodes.add(priorityOperator(priorityOperators.indexOf(sign)-1,tokenBuffer));
                    binaryOperatorNode.setNodes(childrenNodes);
                    variable = binaryOperatorNode;
                }
                case COMPOUND_END, EOF, UNARY_OPERATOR, CONDITION -> {
                    tokenBuffer.prev();
                    return variable;
                }
                default -> {
                    throw new SyntaxException(String.format("Excess \"%s\"",token.getSign()), token.getPosition(), tokensToCode(tokenBuffer.tokenList));
                }
            }
        }
        return null;
    }

    /**
     * @param tokenBuffer буфер с токенами
     * @return Возвращает узел, который является унарным оператором.
     */
    protected Node unaryParsing(TokenBuffer tokenBuffer, String sign) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, CompileException {
        while (tokenBuffer.hasNext()) {
            Token token = tokenBuffer.next();
            switch (token.getLexemeType()) {
                case SPACE -> {
                }
                case UNARY_OPERATOR -> {
                    if(!sign.equals(token.getSign())) {
                        tokenBuffer.prev();
                        return priorityOperator(priorityOperators.indexOf(token.getSign()),tokenBuffer);
                    }
                    UnaryOperatorNode unaryOperatorNode;
                    if(specialOperators.containsKey(token.getSign())) {
                        unaryOperatorNode = (UnaryOperatorNode) specialOperators.get(token.getSign()).getConstructor(String.class).newInstance(token.getSign());
                    }
                    else unaryOperatorNode = new UnaryOperatorNode(token.getSign());
                    unaryOperatorNode.setNode(priorityOperator(priorityOperators.indexOf(sign)-1,tokenBuffer));
                    return unaryOperatorNode;
                }
                default -> {
                    tokenBuffer.prev();
                    return priorityOperator(priorityOperators.indexOf(sign)-1,tokenBuffer);
                }
            }
        }
        return null;
    }


    /**
     * Парсит только переменные и выражения, заключенные в compoundOperators
     * @param tokenBuffer буфер с токенами
     * @return Возвращает узел, который является либо переменной, либо целым выражением.
     */
    protected Node variableParsing(TokenBuffer tokenBuffer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompileException {
        while (tokenBuffer.hasNext()) {
            Token variable = tokenBuffer.next();
            switch (variable.getLexemeType()) {
                case SPACE -> {
                }
                case COMPOUND_START -> {
                    Node node = exprParsing(tokenBuffer);
                    Token compoundEnd = tokenBuffer.next();
                    if (compoundEnd.getLexemeType() != TokenType.COMPOUND_END)
                        throw new SyntaxException("closer compound is missing", compoundEnd.getPosition(), tokensToCode(tokenBuffer.tokenList));
                    return node;
                }
                case CONDITION -> {
                    return new ConditionNode(variable.getSign());
                }
                case UNARY_OPERATOR -> {
                    tokenBuffer.prev();
                    return priorityOperator(priorityOperators.indexOf(variable.getSign()), tokenBuffer);
                }
                default -> {
                    throw new ConditionException("Condition not specified", variable.getPosition(), tokensToCode(tokenBuffer.tokenList));
                }
            }
        }
        return null;
    }

    protected String tokensToCode(List<Token> tokenList) {
        return String.join("",tokenList.stream().map(Token::getSign).toList());
    }
}
