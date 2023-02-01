package thedivazo.conditionhandler.parser.AST;

import lombok.Getter;

import java.util.Objects;

public class UnaryOperationNode extends ExpressionNode {

    @Getter
    protected ExpressionNode argument1 = null;

    public UnaryOperationNode(String name) {
        super(name);
    }


    @Override
    public void addNextNode(ExpressionNode node) {
        if(Objects.isNull(argument1)) argument1 = node;
        else return;
        node.setPrevNode(this);

    }
}
