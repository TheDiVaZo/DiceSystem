package thedivazo.parserexpression.interpreter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.parserexpression.parser.AST.*;
import thedivazo.parserexpression.parser.Node;
import thedivazo.utils.TernaryOperator;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;


/**
 * Класс, который исполняет команды узлов (AST)
 * @param <T> the type that the condition works with.
 * @param <R> the type that returns the condition.
 * @author TheDiVaZo
 * @version 2.0
 */
public class Interpreter<T, R> {

    @RequiredArgsConstructor
    class ConditionName {

        @Getter
        private final String regEx;

        @Getter
        private final BiFunction<T,String,R> condition;

    }

    protected List<ConditionName> listConditionNames = new ArrayList<>();

    @Getter
    @Setter
    protected Function<String, R> alternativeConditionParser;

    protected Map<String, TernaryOperator<R>> listTernaryOperators = new HashMap<>();

    protected Map<String, BinaryOperator<R>> listBinaryOperators = new HashMap<>();

    protected Map<String, UnaryOperator<R>> listUnaryOperators = new HashMap<>();

    protected Map<String, Function<List<R>,R>> listFunctionOperators = new HashMap<>();

    public void addTernaryOperator(String sign1, String sign2, TernaryOperator<R> ternaryOperator) {
        listTernaryOperators.put(sign1+sign2, ternaryOperator);
    }

    public void addUnaryOperator(String sign, UnaryOperator<R> unaryOperator) {
        listUnaryOperators.put(sign, unaryOperator);
    }

    public void addBinaryOperator(String sign, BinaryOperator<R> binaryOperator) {
        listBinaryOperators.put(sign, binaryOperator);
    }

    public void addFunctionOperator(String sign, Function<List<R>,R> functionOperator) {
        listFunctionOperators.put(sign, functionOperator);
    }

    public void addCondition(String regEx, BiFunction<T,String,R> condition) {
        listConditionNames.add(new ConditionName(regEx, condition));
    }

    public R execute(Node mainNode, T input) throws InterpreterException {
        return execute(mainNode, input, null);
    }


    /**
     * Исполняет операторы, выраженные узлами AST дерева. Работает рекурсивно для каждых дочерних узлов родительского узла.
     * @param mainNode корневой узел AST дерева
     * @param input выражение, которое нужно передать в condition
     * @return Возвращает результат работы.
     * @throws InterpreterException выбрасывается, если в AST дереве присутствуют ошибки.
     */
    public R execute(Node mainNode , T input,@Nullable Map<String, R> localConditions) throws InterpreterException {
        if(mainNode instanceof FunctionOperatorNode functionOperatorNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes();
            return listFunctionOperators.get(functionOperatorNode.getNodeName()).apply(childrenNode.stream().map(node -> executeNoException(node, input, localConditions)).toList());
        }
        else if(mainNode instanceof TernaryOperatorNode ternaryOperatorNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes().stream().toList();
            return listTernaryOperators.get(ternaryOperatorNode.getNodeName()).apply(execute(childrenNode.get(0), input, localConditions), execute(childrenNode.get(1), input, localConditions), execute(childrenNode.get(2), input,localConditions));
        }
        else if(mainNode instanceof BinaryOperatorNode binaryOperatorNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes().stream().toList();
            return listBinaryOperators.get(binaryOperatorNode.getNodeName()).apply(execute(childrenNode.get(0), input,localConditions), execute(childrenNode.get(1), input, localConditions));
        }
        else if(mainNode instanceof UnaryOperatorNode unaryOperationNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes().stream().toList();
            return listUnaryOperators.get(unaryOperationNode.getNodeName()).apply(execute(childrenNode.get(0), input,localConditions));
        }
        else if(mainNode instanceof ConditionNode conditionNode) {
            if(!Objects.isNull(localConditions) && localConditions.containsKey(conditionNode.getNodeName())) return localConditions.get(conditionNode.getNodeName());
            if(listConditionNames.stream().anyMatch(cn ->conditionNode.getNodeName().matches(cn.getRegEx()))) {
                ConditionName conditionName = listConditionNames.stream().filter(cn ->conditionNode.getNodeName().matches(cn.getRegEx())).findFirst().orElse(null);
                assert conditionName != null;
                return conditionName.getCondition().apply(input, conditionNode.getNodeName());
            }
            else if(Objects.isNull(alternativeConditionParser)) throw new InterpreterException(String.format("Unknown condition: %s", mainNode.getNodeName()));
            else return alternativeConditionParser.apply(conditionNode.getNodeName());
        }
        else throw new InterpreterException(String.format("Unknown node: %s", mainNode));
    }

    protected R executeNoException(Node mainNode , T input, Map<String, R> localConditions) {
        try {
            return execute(mainNode, input, localConditions);
        } catch (InterpreterException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
