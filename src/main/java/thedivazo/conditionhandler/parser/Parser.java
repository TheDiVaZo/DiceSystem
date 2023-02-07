package thedivazo.conditionhandler.parser;

import thedivazo.conditionhandler.lexer.Token;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.AST.BinaryOperatorNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;
import thedivazo.conditionhandler.parser.AST.OperatorNode;
import thedivazo.conditionhandler.parser.AST.UnaryOperatorNode;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Parser {
    /*
    A || B || C || D
    ((A || B) || C) || D
     */

    /*
    RULES PARSING

    EXPR -> BINARY* EOF;
    BINARY -> PROM ('B_OP' PROM)*
    UNARY -> 'U_OP' BINARY
    PROM -> UNARY | VARIABLE
    VARIABLE -> CONDITION | 'compoundStart' EXPR 'compoundEnd';
    CONDITION -> [a-zA-Z_0-9]+
     */


    /**
     * Здесь нужно указать приоритет каждого оператора.
     * Операторы без указанного приоритета, по умолчанию имеют самый низкий приоритет.
     */
    //protected Map<String, Integer> priorityOperators = new HashMap<>();


    /**
     * Если оператор является нестандартным, то можно написать для него свою реализацию
     */
    protected Map<String, Class<? extends OperatorNode >> specialOperators = new HashMap<>();

    /**
     * @param tokenList буфер с токенами
     * @return Возвращает коренной узел, который является вершиной AST дерева.
     */
    public Node parsing(List<Token> tokenList) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TokenBuffer tokenBuffer = new TokenBuffer(tokenList);
        return exprParsing(tokenBuffer);
    }

    /**
     * @param tokenBuffer буфер с токенами
     * @return Возвращает главный узел из данного набора токенов.
     */
    public Node exprParsing(TokenBuffer tokenBuffer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Token token = tokenBuffer.next();
        if (token.getLexemeType() == TokenType.EOF) {
            return new ConditionNode("");
        } else {
            tokenBuffer.prev();
            return binaryParsing(tokenBuffer);
        }
    }

    /**
     * @param tokenBuffer буфер с токенами
     * @return Возвращает узел, который является либо бинарным оператором, либо значением из {@link Parser#promParsing(TokenBuffer)}
     */
    public Node binaryParsing(TokenBuffer tokenBuffer) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Node variable = promParsing(tokenBuffer);
        while (tokenBuffer.hasNext()) {
            Token token = tokenBuffer.next();
            switch (token.getLexemeType()) {
                case SPACE -> {}
                case BINARY_OPERATOR -> {
                    BinaryOperatorNode binaryOperatorNode;
                    if(specialOperators.containsKey(token.getSign())) {
                        binaryOperatorNode = (BinaryOperatorNode) specialOperators.get(token.getSign()).getConstructor(String.class).newInstance(token.getSign());
                    }
                    else binaryOperatorNode = new BinaryOperatorNode(token.getSign());
                    Set<Node> childrenNodes = new LinkedHashSet<>();
                    childrenNodes.add(variable);
                    childrenNodes.add(promParsing(tokenBuffer));
                    binaryOperatorNode.setNodes(childrenNodes);
                    variable = binaryOperatorNode;
                }
                case COMPOUND_END, EOF, UNARY_OPERATOR, CONDITION -> {
                    tokenBuffer.prev();
                    return variable;
                }
                default -> {
                    System.out.println(token.getLexemeType());
                    throw new RuntimeException("dd2"); //TODO: Сделать нормальную обработку
                }
            }
        }
        return null;
    }

    /**
     * @param tokenBuffer буфер с токенами
     * @return Возвращает узел, который является унарным оператором.
     */
    public Node unaryParsing(TokenBuffer tokenBuffer) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        while (tokenBuffer.hasNext()) {
            Token token = tokenBuffer.next();
            switch (token.getLexemeType()) {
                case SPACE -> {
                }
                case UNARY_OPERATOR -> {
                    UnaryOperatorNode unaryOperatorNode;
                    if(specialOperators.containsKey(token.getSign())) {
                        unaryOperatorNode = (UnaryOperatorNode) specialOperators.get(token.getSign()).getConstructor(String.class).newInstance(token.getSign());
                    }
                    else unaryOperatorNode = new UnaryOperatorNode(token.getSign());
                    unaryOperatorNode.setNode(binaryParsing(tokenBuffer));
                    return unaryOperatorNode;
                }
                default -> {
                    System.out.println(token.getLexemeType());
                    throw new RuntimeException("dd2"); //TODO: Сделать нормальную обработку
                }
            }
        }
        return null;
    }

    /**
     * @param tokenBuffer Буфер с токенами
     * @return Возвращает значение {@link Parser#variableParsing(TokenBuffer)} или {@link Parser#unaryParsing(TokenBuffer)}
     */
    public Node promParsing(TokenBuffer tokenBuffer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        while (tokenBuffer.hasNext()) {
            Token token = tokenBuffer.next();
            switch (token.getLexemeType()) {
                case SPACE -> {}
                case UNARY_OPERATOR -> {
                    tokenBuffer.prev();
                    return unaryParsing(tokenBuffer);
                }
                default -> {
                    tokenBuffer.prev();
                    return variableParsing(tokenBuffer);
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
    public Node variableParsing(TokenBuffer tokenBuffer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        while (tokenBuffer.hasNext()) {
            Token variable = tokenBuffer.next();
            switch (variable.getLexemeType()) {
                case SPACE -> {
                }
                case COMPOUND_START -> {
                    Node node = exprParsing(tokenBuffer);
                    Token compoundEnd = tokenBuffer.next();
                    if (compoundEnd.getLexemeType() != TokenType.COMPOUND_END)
                        throw new RuntimeException("dd1"); //TODO: сделать нормальный вызов ошибки
                    return node;
                }
                case CONDITION -> {
                    return new ConditionNode(variable.getSign());
                }
                default -> {
                    System.out.println(variable.getLexemeType());
                    throw new RuntimeException("dd2"); //TODO: Сделать нормальную обработку
                }
            }
        }
        return null;
    }
}
