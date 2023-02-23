package thedivazo.conditionhandler.parser.AST;

import thedivazo.conditionhandler.parser.Node;

import java.util.Set;

public class BinaryOperatorNode extends OperatorNode {

    public BinaryOperatorNode(String nodeName) {
        super(nodeName);
    }

    @Override
    public boolean setNodes(Set<Node> nodes) {
        if(nodes.size()!=2) throw new IllegalArgumentException("A binary operator must have 2 arguments.");
        return super.setNodes(nodes);
    }
}
