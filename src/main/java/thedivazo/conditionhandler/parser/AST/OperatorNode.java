package thedivazo.conditionhandler.parser.AST;

import thedivazo.conditionhandler.parser.Node;

import java.awt.*;
import java.util.*;

public class OperatorNode extends Node {

    private LinkedHashSet<Node> childrenNodes = new LinkedHashSet<>(){{add(null);add(null);}};
    public OperatorNode(String nodeName) {
        super(nodeName);
    }

    @Override
    public boolean setNodes(Node... nodes) {
        childrenNodes.clear();
        childrenNodes.addAll(Arrays.stream(nodes).toList());
        return true;
    }

    @Override
    public Set<Node> getChildrenNodes() {
        return Collections.unmodifiableSet(childrenNodes);
    }

    @Override
    public String toString() {
        return nodeName + String.format("(%s)", String.join(",", getChildrenNodes().stream().map(Node::toString).toList()));
    }
}
