package thedivazo.conditionhandler.parser.AST;

import thedivazo.conditionhandler.parser.Node;

import java.awt.*;
import java.util.*;
import java.util.List;

public class OperatorNode extends Node {

    private List<Node> childrenNodes = new ArrayList<>();
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
    public List<Node> getChildrenNodes() {
        return Collections.unmodifiableList(childrenNodes);
    }

    @Override
    public String toString() {
        return nodeName + String.format("(%s)", String.join(",", getChildrenNodes().stream().map(Node::toString).toList()));
    }
}
