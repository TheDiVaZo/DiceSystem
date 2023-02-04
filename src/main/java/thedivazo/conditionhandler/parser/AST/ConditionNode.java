package thedivazo.conditionhandler.parser.AST;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ConditionNode extends ExpressionNode {

    protected Set<ExpressionNode> setNodes = new LinkedHashSet<>();

    /**
     * @param name Имя переменной
     */
    public ConditionNode(String name) {
        super(name);
    }

    @Override
    public void addNextNode(ExpressionNode node) {
        setNodes.add(node);
    }

    @Override
    public Set<ExpressionNode> getNextNodes() {
        return Collections.unmodifiableSet(setNodes);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name+"\n");
        if(!setNodes.isEmpty()) {
            for (ExpressionNode node : setNodes) {
                result.append("  ").append(String.join("   \n", node.toString().split("\n"))).append("\n").append("\n");
            }
        }
        return result.toString();
    }
}
