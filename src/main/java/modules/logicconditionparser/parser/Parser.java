package modules.logicconditionparser.parser;

import modules.logicconditionparser.lexer.Token;
import modules.logicconditionparser.lexer.TokenType;

import java.util.*;

public class Parser {

    private Parser() {
    }

    protected static Map<Token, Integer> getCompoundOperatorsMapID(List<Token> tokenList) {
        List<Token> compoundOperatorList = tokenList.stream().filter(operator -> operator.getTokenType().equals(TokenType.COMPOUND_OPERATOR_START) || operator.getTokenType().equals(TokenType.COMPOUND_OPERATOR_END)).toList();
        Map<Token, Integer> compoundOperatorIDMap = new LinkedHashMap<>();
        int id = 0;
        Deque<Integer> idList = new ArrayDeque<>();
        for (Token token : compoundOperatorList) {
            if(token.getTokenType().equals(TokenType.COMPOUND_OPERATOR_START)) {
                compoundOperatorIDMap.put(token, id);
                idList.add(id);
                id++;
            } else {
                compoundOperatorIDMap.put(token, idList.pollLast());
            }
        }
        return compoundOperatorIDMap;
    }

    protected static List<Token> removeExtraCompound(List<Token> tokenList) {
        if(tokenList.isEmpty() || !tokenList.get(0).getTokenType().equals(TokenType.COMPOUND_OPERATOR_START) || !tokenList.get(tokenList.size()-1).getTokenType().equals(TokenType.COMPOUND_OPERATOR_END)) return tokenList;
        Map<Token, Integer> compoundOperatorIDMap = getCompoundOperatorsMapID(tokenList);
        while (true) {
            if(!tokenList.isEmpty()) {
                Token compoundStart = tokenList.get(0);
                Token compoundEnd = tokenList.get(tokenList.size() - 1);
                if (!compoundStart.getTokenType().equals(TokenType.COMPOUND_OPERATOR_START) || !compoundEnd.getTokenType().equals(TokenType.COMPOUND_OPERATOR_END))
                    break;

                if (compoundOperatorIDMap.get(compoundStart).equals(compoundOperatorIDMap.get(compoundEnd))) {
                    tokenList = tokenList.subList(1, tokenList.size() - 1);
                } else break;
            }
        }

        return tokenList;

    }

    protected static Token getPriotiryToken(List<Token> tokenList) {
        Token priorityToken = null;
        int flagCompound = 0;
        if(tokenList.size()==1) return tokenList.get(0);
        for (int i = 0; i < tokenList.size(); i++) {
            Token currentToken = tokenList.get(i);
            if (currentToken.getTokenType().equals(TokenType.COMPOUND_OPERATOR_START)) flagCompound++;
            if (currentToken.getTokenType().equals(TokenType.COMPOUND_OPERATOR_END)) flagCompound--;
            if (!currentToken.getTokenType().equals(TokenType.CONDITION_NAME) && flagCompound == 0) {
                if (Objects.isNull(priorityToken)) priorityToken = currentToken;
                else priorityToken = currentToken.getPriority() < priorityToken.getPriority() ? currentToken : priorityToken;
            }
        }
        return priorityToken;
    }

    public static Node parseToAST(List<Token> tokenList) {
        tokenList = removeExtraCompound(tokenList);
        if(tokenList.isEmpty()) return null;
        Token priorityToken = getPriotiryToken(tokenList);

        if(Objects.isNull(priorityToken)) return null;
        int index = tokenList.indexOf(priorityToken);

        Node node = new Node(priorityToken.getTokenType(),priorityToken.getSign());

        if (priorityToken.getTokenType() == TokenType.BINARY_OPERATOR) {
            node.addNextNode(parseToAST(tokenList.subList(0, index)));
            node.addNextNode(parseToAST(tokenList.subList(index + 1, tokenList.size())));
        } else if (priorityToken.getTokenType() == TokenType.UNARY_OPERATOR) {
            node.addNextNode(parseToAST(tokenList.subList(index + 1, tokenList.size())));
        }

        return node;
    }

}