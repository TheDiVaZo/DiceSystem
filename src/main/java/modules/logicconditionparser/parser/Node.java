package modules.logicconditionparser.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


public class Node {
    public boolean hasNext() {
        return !Objects.isNull(nextNode);
    }

    public Node(NodeType nodeType, Node nextNode, Node prevNode, String nameNode) {
        this.nodeType = nodeType;
        setNextNode(nextNode);
        setPrevNode(prevNode);
        this.nameNode = nameNode;
    }

    @Getter
    private NodeType nodeType;
    @Getter
    private Node nextNode;
    public void setNextNode(Node nextNode) {
        if(Objects.isNull(nextNode)) return;
        this.nextNode = nextNode;
        nextNode.prevNode = this;
    }

    @Getter
    private Node prevNode;
    public void setPrevNode(Node prevNode) {
        if(Objects.isNull(prevNode)) return;
        this.prevNode = prevNode;
        nextNode.nextNode = this;
    }

    @Getter
    String nameNode;

    @Override
    public String toString() {
        return "Node{\n" +
                "nodeType=" + nodeType + "\n" +
                ", nameNode='" + nameNode + "'\n" +
                "},\n" +Objects.toString(getNextNode(),"null");
    }
}
