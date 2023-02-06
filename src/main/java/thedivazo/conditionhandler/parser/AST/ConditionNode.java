package thedivazo.conditionhandler.parser.AST;

import java.util.Collections;
import java.util.Deque;
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
        throw new UnsupportedOperationException("Condition don't have next node");
    }

    @Override
    public Deque<ExpressionNode> getNextNodes() {
        throw new UnsupportedOperationException("Condition don't have next node");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name+"\n");
        return result.toString();
    }
}
