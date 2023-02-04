package thedivazo.conditionhandler.parser.AST;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
        node.prevNode = this;

    }

    @Override
    public Set<ExpressionNode> getNextNodes() {
        return new HashSet<>(){{add(argument1);}};
    }

    @Override
    public String toString() {
        String result = name+"\n";
        result +=  String.join("   \n",argument1.toString().split("\n"))+"\n";
        return result;
    }
}
