package thedivazo.conditionhandler.parser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.list.SetUniqueList;
import thedivazo.conditionhandler.exception.CompileException;
import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.lexer.Lexer;
import thedivazo.conditionhandler.lexer.Token;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.AST.BinaryOperatorNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;
import thedivazo.conditionhandler.parser.AST.UnaryOperatorNode;

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

    /*
         BINARY_OPERATOR: OPERATOR_-1 ("operand" OPERATOR_-1)*
         UNARY_OPERATOR: "operand"? OPERATOR_-1

         CONDITION: [a-zA-Z0-9\:]+ |  (EXPRESS)
     */

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class OperatorData {
        private String signOperator;
        private OperatorType operatorType;

        @Override
        public String toString() {
            return signOperator;
        }
    }


    private final List<Set<OperatorData>> listOfPriorityOperator = SetUniqueList.setUniqueList(new ArrayList<>());


    /**
     * Метод, позволяющий добавить токен(ы) оператора(ов) в список приоритета.
     * Чем позже был добавлен оператор(ы) в список, тем более он(и) приоритетен(ны).
     * @param operatorData Массив с неповторяющимися операторами, которые вы хотите добавить в список приоритета
     * @return возвращает состояние добавления
     */
    public boolean addOperator(OperatorData... operatorData) {
        return listOfPriorityOperator.add(Arrays.stream(operatorData).collect(Collectors.toSet()));
    }

    /**
     * @param tokenList Массив с токенами, который нужно преобразовать в AST дерево
     * @return Возвращает головной узел
     */
    public Node parsing(List<Token> tokenList) throws CompileException {
        return expr(new TokenBuffer(tokenList));
    }

    public Node expr(TokenBuffer tokenBuffer) throws CompileException {
        if(tokenBuffer.tokenList.isEmpty()) throw new SyntaxException("The expression cannot be empty",0,tokenBuffer.tokensToCode());
        if(tokenBuffer.next().getLexemeType().equals(TokenType.EOF)) throw new SyntaxException("The expression cannot be empty",tokenBuffer.current().getPosition(),tokenBuffer.tokensToCode());
        tokenBuffer.prev();
        return operator(tokenBuffer, listOfPriorityOperator.size()-1);
    }

    public Set<OperatorData> getOperatorForIndex(int indexPriority) {
        return listOfPriorityOperator.get(indexPriority);
    }

    public Node binaryOperator(TokenBuffer tokenBuffer, Set<OperatorData> operatorsData, int indexPriority) throws CompileException {
        Node prevNode = operator(tokenBuffer, indexPriority-1);
        while (tokenBuffer.hasNext()) {
            Token token = tokenBuffer.next();
            if(operatorsData.stream().anyMatch(operatorData -> operatorData.getSignOperator().equals(token.getSign())) && token.lexemeType().equals(TokenType.OPERATOR)) {
                Node firstOperatorArgument = prevNode;
                Node secondOperatorArgument = operator(tokenBuffer, indexPriority-1);
                BinaryOperatorNode binaryOperatorNode = new BinaryOperatorNode(token.getSign());
                binaryOperatorNode.setNodes(new HashSet<>(){{add(firstOperatorArgument);add(secondOperatorArgument);}});
                prevNode = binaryOperatorNode;
            }
            else {
                tokenBuffer.prev();
                break;
            }
        }
        return prevNode;
    }

    public Node unaryOperator(TokenBuffer tokenBuffer, Set<OperatorData> operatorsData, int indexPriority) throws CompileException {
        Token token = tokenBuffer.next();
        Node argument = null;
        if(operatorsData.stream().anyMatch(operatorData -> operatorData.getSignOperator().equals(token.getSign())) && token.getLexemeType().equals(TokenType.OPERATOR)) {
            UnaryOperatorNode unaryOperatorNode = new UnaryOperatorNode(token.getSign());
            argument = operator(tokenBuffer, indexPriority-1);
            unaryOperatorNode.setNode(argument);
            return unaryOperatorNode;
        }
        tokenBuffer.prev();
        argument = operator(tokenBuffer, indexPriority-1);
        return argument;
    }

    public Node operator(TokenBuffer tokenBuffer,int indexPriority) throws CompileException {
        if(indexPriority < 0) return factor(tokenBuffer);
        Set<OperatorData> operatorsData = getOperatorForIndex(indexPriority);
        if(operatorsData.stream().allMatch(operatorData -> operatorData.getOperatorType().equals(OperatorType.BINARY))) return binaryOperator(tokenBuffer, operatorsData, indexPriority);
        if(operatorsData.stream().allMatch(operatorData -> operatorData.getOperatorType().equals(OperatorType.UNARY))) return unaryOperator(tokenBuffer, operatorsData, indexPriority);
        else throw new CompileException("Operators of different kinds must have different precedence");
    }




    public Node factor(TokenBuffer tokenBuffer) throws CompileException {
        Token currentToken = tokenBuffer.next();
        switch (currentToken.getLexemeType()) {
            case CONDITION -> {
                return new ConditionNode(currentToken.getSign());
            }

            case COMPOUND_START -> {
                Node compoundNode = expr(tokenBuffer);
                Token nextToken = tokenBuffer.next();
                if(!nextToken.getLexemeType().equals(TokenType.COMPOUND_END)) throw new SyntaxException("Missing closing compound", nextToken.getPosition(), tokenBuffer.tokensToCode());
                return compoundNode;
            }
            default -> throw new SyntaxException("Unknown condition", currentToken.getPosition(), tokenBuffer.tokensToCode());

        }
    }

    protected String tokensToCode(List<Token> tokenList) {
        return String.join("",tokenList.stream().map(Token::getSign).toList());
    }
}
