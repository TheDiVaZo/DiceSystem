package modules.logicconditionparser.parser;

public class NodeCondition extends Node {
    public NodeCondition(NodeType nodeType, Node nextNode, Node prevNode, String nameNode) {
        super(nodeType, nextNode, prevNode, nameNode);
    }
}

/*
    (cond1&&cond2) || cond3

    Operation ||:
        Operation &&:
            Condition1,
            Condition2,
        Condition1

 */
