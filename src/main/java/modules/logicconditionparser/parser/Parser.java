package modules.logicconditionparser.parser;

import modules.logicconditionparser.lexer.Lexeme;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Parser {
    public static Node parse(List<Lexeme> lexemeList) throws Exception {
        Lexeme lexeme = null;
        int index = 0;
        if(lexemeList.size()==1) {
            lexeme = lexemeList.get(0);
        }
        else if(lexemeList.size()>1) {
            lexeme = lexemeList.stream().max(Comparator.comparingInt(Lexeme::getPriority)).get();
            index = lexemeList.indexOf(lexeme);
        }
        else return null;
        switch (lexeme.getToken()) {
            case CONDITION_NAME -> {
                NodeCondition nodeCondition = new NodeCondition(NodeType.CONDITION_NAME, null, null, lexeme.getSign());
                return nodeCondition;
            }
            case BINARY_OPERATION -> {
                NodeOperation nodeOperation = new NodeOperation(NodeType.BINARY_OPERATION, null, null, lexeme.getSign());
                Node argument1 = parse(lexemeList.subList(0,index));
                Node argument2 = parse(lexemeList.subList(index+1 == 0 ?0:index+1,lexemeList.size()));
                if(Objects.isNull(argument1) || Objects.isNull(argument2)) {
                    throw new Exception("Ошибка аргумента " + lexeme.getSign());
                }
                argument1.setNextNode(argument2);
                nodeOperation.setConditionNodes(argument1);
                return nodeOperation;
            }
            case UNARY_OPERATION -> {
                NodeOperation nodeOperation = new NodeOperation(NodeType.UNARY_OPERATION, null, null, lexeme.getSign());
                Node argument2 = parse(lexemeList.subList(index+1 == 0 ?0:index+1,lexemeList.size()));
                if(Objects.isNull(argument2)) {
                    throw new Exception("Ошибка аргумента " + lexeme.getSign());
                }
                nodeOperation.setConditionNodes(argument2);
                return nodeOperation;
            }
            default -> {throw new UnsupportedOperationException("Такая операция неподдерживается");}
        }


    }

}

/*
!cond1 || !cond2 && cond3

!cond1 && cond2 || cond3

! - 0
&& - 1
|| - 2


cond1
&&

 */
