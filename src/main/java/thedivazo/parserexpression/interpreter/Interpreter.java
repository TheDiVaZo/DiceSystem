package thedivazo.parserexpression.interpreter;

import lombok.Getter;
import lombok.Setter;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.parserexpression.parser.AST.BinaryOperatorNode;
import thedivazo.parserexpression.parser.AST.ConditionNode;
import thedivazo.parserexpression.parser.AST.FunctionOperatorNode;
import thedivazo.parserexpression.parser.AST.UnaryOperatorNode;
import thedivazo.parserexpression.parser.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;


/**
 * Класс, который исполняет команды узлов (AST)
 * @param <T> the type that the condition works with.
 * @param <R> the type that returns the condition.
 * @author TheDiVaZo
 * @version 1.0
 */
public class Interpreter<T, R> {

    protected Map<String, Function<T, R>> listConditionNames = new HashMap<>();

    @Getter
    @Setter
    protected Function<String, R> alternativeConditionParser;

    protected Map<String, BinaryOperator<R>> listBinaryOperators = new HashMap<>();

    protected Map<String, UnaryOperator<R>> listUnaryOperators = new HashMap<>();

    protected Map<String, Function<List<R>,R>> listFunctionOperators = new HashMap<>();

    public void addUnaryOperator(String sign, UnaryOperator<R> unaryOperator) {
        listUnaryOperators.put(sign, unaryOperator);
    }

    public void addBinaryOperator(String sign, BinaryOperator<R> binaryOperator) {
        listBinaryOperators.put(sign, binaryOperator);
    }

    public void addFunctionOperator(String sign, Function<List<R>,R> functionOperator) {
        listFunctionOperators.put(sign, functionOperator);
    }

    public void addCondition(String sign, Function<T, R> condition) {
        listConditionNames.put(sign, condition);
    }


    /**
     * Исполняет операторы, выраженные узлами AST дерева. Работает рекурсивно для каждых дочерних узлов родительского узла.
     * @param mainNode корневой узел AST дерева
     * @param input выражение, которое нужно передать в condition
     * @return Возвращает результат работы.
     * @throws InterpreterException выбрасывается, если в AST дереве присутствуют ошибки.
     */
    public R execute(Node mainNode , T input) throws InterpreterException {
        if(mainNode instanceof FunctionOperatorNode functionOperatorNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes();
            return listFunctionOperators.get(functionOperatorNode.getNodeName()).apply(childrenNode.stream().map(node -> executeNoException(node, input)).toList());
        }
        if(mainNode instanceof BinaryOperatorNode binaryOperatorNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes().stream().toList();
            return listBinaryOperators.get(binaryOperatorNode.getNodeName()).apply(execute(childrenNode.get(0), input), execute(childrenNode.get(1), input));
        }
        if(mainNode instanceof UnaryOperatorNode unaryOperationNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes().stream().toList();
            return listUnaryOperators.get(unaryOperationNode.getNodeName()).apply(execute(childrenNode.get(0), input));
        }
        else if(mainNode instanceof ConditionNode conditionNode) {
            if(listConditionNames.containsKey(conditionNode.getNodeName())) return listConditionNames.get(conditionNode.getNodeName()).apply(input);
            else if(Objects.isNull(alternativeConditionParser)) throw new InterpreterException(String.format("Unknown condition: %s", mainNode.getNodeName()));
            else return alternativeConditionParser.apply(conditionNode.getNodeName());
        }
        else throw new InterpreterException(String.format("Unknown node: %s", mainNode.getNodeName()));
    }

    protected R executeNoException(Node mainNode , T input) {
        try {
            return execute(mainNode, input);
        } catch (InterpreterException e) {
            throw new RuntimeException(e);
        }
    }

}
