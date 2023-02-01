package thedivazo.conditionhandler.parser.AST;

import lombok.Getter;

import java.util.Objects;

public class BinaryOperationNode extends ExpressionNode {

    @Getter
    protected ExpressionNode argument1 = null;

    @Getter
    protected ExpressionNode argument2 = null;

    public BinaryOperationNode(String name) {
        super(name);
    }


    @Override
    public void addNextNode(ExpressionNode node) {
        if(Objects.isNull(argument1)) argument1 = node;
        else if(Objects.isNull(argument2)) argument2 = node;
        else return;
        node.setPrevNode(this);

    }
}
