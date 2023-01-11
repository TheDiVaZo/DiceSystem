package modules.logicconditionparser.parser;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class NodeOperation extends Node {

    @Getter
    @Setter
    private Node conditionNodes;

    public NodeOperation(NodeType nodeType, Node nextNode, Node prevNode, String nameNode) {
        super(nodeType, nextNode, prevNode, nameNode);
    }

    @Override
    public String toString() {
        String condNodesText = "";
        Node currentNode = this.conditionNodes;
        while (true) {
           condNodesText += String.join("\n", Arrays.stream(currentNode.toString().split("\n")).map(str->"   "+str).toList());
           if(currentNode.hasNext()) currentNode = currentNode.getNextNode();
           else break;
        }
        return "OperationNode{\n" +
                "nameNode='" + nameNode + "'" + "\n"+
                "conditionNodes=\n" + condNodesText + "\n"+
                '}';
    }
}
