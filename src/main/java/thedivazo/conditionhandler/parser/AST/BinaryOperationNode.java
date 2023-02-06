package thedivazo.conditionhandler.parser.AST;

import lombok.Getter;

import java.util.*;

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
        node.prevNode = this;

    }

    @Override
    public Deque<ExpressionNode> getNextNodes() {
        return new ArrayDeque<>(){{add(argument1);add(argument2);}};
    }

    @Override
    public String toString() {
        String result = name+"\n";
        result += String.join("   \n",argument1.toString().split("\n"))+"\n";
        result +=  String.join("   \n",argument2.toString().split("\n"))+"\n";
        return result;
    }
}
