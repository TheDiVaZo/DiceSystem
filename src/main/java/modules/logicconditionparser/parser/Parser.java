package modules.logicconditionparser.parser;

import modules.logicconditionparser.lexer.Token;
import modules.logicconditionparser.lexer.TokenType;

import java.util.List;
import java.util.Objects;

public class Parser {

    private Parser() {
    }

    public static Node parseToAST(List<Token> tokenList) throws Exception {
        if(tokenList.isEmpty()) return null;
        if(tokenList.get(0).getTokenType().equals(TokenType.CONDITION_NAME)) return new Node(TokenType.CONDITION_NAME, tokenList.get(0).getSign());
        Token priorityToken = null;

        int flagCompound = 0;
        for (int i = 0; i < tokenList.size(); i++) {
            Token currentToken = tokenList.get(i);
            if (currentToken.getTokenType().equals(TokenType.COMPOUND_OPERATOR_START)) {
                if(i==0) continue;
                 else flagCompound++;
            }
            if (currentToken.getTokenType().equals(TokenType.COMPOUND_OPERATOR_END)) {
                if(i == tokenList.size()-1) continue;
                else flagCompound--;
            }
            if (!currentToken.getTokenType().equals(TokenType.CONDITION_NAME) && flagCompound == 0) {
                if (Objects.isNull(priorityToken)) priorityToken = currentToken;
                else priorityToken = currentToken.getPriority() < priorityToken.getPriority() ? currentToken : priorityToken;
            }
        }
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

/*
todo:
!cond1 || !cond2 && cond3

!cond1 && cond2 || cond3

!cond1 || (cond1 && cond2) || cond3

! - 0
&& - 1
|| - 2


cond1
&&

 */
