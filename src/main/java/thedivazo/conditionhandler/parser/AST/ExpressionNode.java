package thedivazo.conditionhandler.parser.AST;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Deque;
import java.util.Set;


/**
 * Реализация Node. Каждый узел хранит множество узлов, на которое он ссылается и хранит 1 узел, тот, кто на него ссылается.
 * Но
 */
@RequiredArgsConstructor
public abstract class ExpressionNode {


    /**
     * Имя узла
     */
    @Getter
    protected final String name;

    protected ExpressionNode prevNode = null;

    public void setPrevNode(ExpressionNode node) {
        if(this.prevNode == node) return;
        this.prevNode = node;
        node.addNextNode(this);
    }

    public abstract void addNextNode(ExpressionNode node);

    public abstract Deque<ExpressionNode> getNextNodes();

    @Override
    public String toString() {
        return getName()+"\n";
    }
}
