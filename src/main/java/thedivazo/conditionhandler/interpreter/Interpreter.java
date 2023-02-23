package thedivazo.conditionhandler.interpreter;

import lombok.Getter;
import org.bukkit.entity.Player;
import thedivazo.conditionhandler.exception.InterpreterException;
import thedivazo.conditionhandler.parser.AST.BinaryOperatorNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;
import thedivazo.conditionhandler.parser.AST.UnaryOperatorNode;
import thedivazo.conditionhandler.parser.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    protected Map<String, BinaryOperator<R>> listBinaryOperators = new HashMap<>();

    protected Map<String, UnaryOperator<R>> listUnaryOperators = new HashMap<>();

    public void addUnaryOperator(String sign, UnaryOperator<R> unaryOperator) {
        listUnaryOperators.put(sign, unaryOperator);
    }

    public void addBinaryOperator(String sign, BinaryOperator<R> binaryOperator) {
        listBinaryOperators.put(sign, binaryOperator);
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
        if(mainNode instanceof BinaryOperatorNode binaryOperatorNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes().stream().toList();
            return listBinaryOperators.get(binaryOperatorNode.getNodeName()).apply(execute(childrenNode.get(0), input), execute(childrenNode.get(1), input));
        }
        if(mainNode instanceof UnaryOperatorNode unaryOperationNode) {
            List<Node> childrenNode = mainNode.getChildrenNodes().stream().toList();
            return listUnaryOperators.get(unaryOperationNode.getNodeName()).apply(execute(childrenNode.get(0), input));
        }
        else if(mainNode instanceof ConditionNode conditionNode) {
            return listConditionNames.get(conditionNode.getNodeName()).apply(input);
        }
        else throw new InterpreterException(String.format("Unknow node: %s", mainNode.getNodeName()));
    }

}
