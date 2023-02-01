package thedivazo.conditionhandler.parser.AST;

import lombok.RequiredArgsConstructor;

public class ConditionNode extends ExpressionNode {

    /**
     * @param name Имя переменной
     */
    public ConditionNode(String name) {
        super(name);
    }

    @Override
    public void addNextNode(ExpressionNode node) {
        throw new UnsupportedOperationException("this operation is not supported in ConditionGroupNode");
    }
}
