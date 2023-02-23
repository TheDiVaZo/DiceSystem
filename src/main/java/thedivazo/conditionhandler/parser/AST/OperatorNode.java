package thedivazo.conditionhandler.parser.AST;

import thedivazo.conditionhandler.parser.Node;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class OperatorNode extends Node {

    private LinkedHashSet<Node> childrenNodes = new LinkedHashSet<>(){{add(null);add(null);}};
    public OperatorNode(String nodeName) {
        super(nodeName);
    }

    @Override
    public boolean setNodes(Set<Node> nodes) {
        if(nodes.stream().anyMatch(this::equals)) throw new IllegalArgumentException(String.format("A operator \"%s\" cannot be its own argument.", nodeName));
        childrenNodes.clear();
        childrenNodes.addAll(nodes);
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
