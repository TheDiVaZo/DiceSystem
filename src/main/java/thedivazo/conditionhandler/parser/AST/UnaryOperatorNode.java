package thedivazo.conditionhandler.parser.AST;

import thedivazo.conditionhandler.parser.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UnaryOperatorNode extends OperatorNode {

    public UnaryOperatorNode(String nodeName) {
        super(nodeName);
    }

    @Override
    public boolean setNodes(Set<Node> nodes) {
        if(nodes.size()!=1) throw new IllegalArgumentException(String.format("A unary operator \"%s\" must have 1 arguments.", nodeName));
        return super.setNodes(nodes);
    }

    public boolean setNode(Node node) {
        return super.setNodes(new HashSet<>(){{add(node);}});
    }
}
