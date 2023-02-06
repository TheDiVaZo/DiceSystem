package thedivazo.conditionhandler.interpreter;

import org.bukkit.entity.Player;
import thedivazo.conditionhandler.condition.Condition;
import thedivazo.conditionhandler.exception.UnknownConditionException;
import thedivazo.conditionhandler.exception.UnknownOperatorException;
import thedivazo.conditionhandler.parser.AST.BinaryOperationNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;
import thedivazo.conditionhandler.parser.AST.ExpressionNode;
import thedivazo.conditionhandler.parser.AST.UnaryOperationNode;

import java.util.*;

public class Interpreter {
    protected Map<String, Condition> conditionNamesMap = new HashMap<>(){{
        put("true", player -> true);
        put("false", player -> false);
    }};
    protected Map<String, Operator> operatorNamesMap = new HashMap<>();

    interface Operator {}

    @FunctionalInterface
    interface BinaryOperator extends Operator {
        boolean exclude(boolean arg1, boolean arg2);
    }

    @FunctionalInterface
    interface UnaryOperator extends Operator {
        boolean exclude(boolean arg1);
    }

    public void setOperator(String name, Operator operator) {
        operatorNamesMap.put(name, operator);
    }
    public void setCondition(String name, Condition condition) {
        conditionNamesMap.put(name, condition);
    }
    public void removeOperator(String name) {
        operatorNamesMap.remove(name);
    }
    public void removeCondition(String name) {
        conditionNamesMap.remove(name);
    }

    boolean exclude(ExpressionNode node, Player player) throws UnknownConditionException, UnknownOperatorException {
        if(Objects.isNull(node)) throw new IllegalArgumentException("Node don't have been null");
        if(node instanceof ConditionNode) {
            if(!conditionNamesMap.containsKey(node.getName())) throw new UnknownConditionException(String.format("unknown condition: %s", node.getName()));
            return conditionNamesMap.get(node.getName()).getResult(player);
        }
        if(node instanceof BinaryOperationNode || node instanceof UnaryOperationNode) {
            if(!operatorNamesMap.containsKey(node.getName())) throw new UnknownOperatorException(String.format("Unknown operator: %s",node.getName()));
            Operator operator = operatorNamesMap.get(node.getName());
            if(operator instanceof BinaryOperator binaryOperator) {
                Deque<ExpressionNode> nodeSet = node.getNextNodes();
                return binaryOperator.exclude(exclude(nodeSet.pop(), player), exclude(nodeSet.pop(), player));
            }
            else if(operator instanceof UnaryOperator unaryOperator) {
                Deque<ExpressionNode> nodeSet = node.getNextNodes();
                return unaryOperator.exclude(exclude(nodeSet.pop(), player));
            }
        }
    }
}
