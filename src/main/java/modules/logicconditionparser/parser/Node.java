package modules.logicconditionparser.parser;

import modules.logicconditionparser.lexer.TokenType;

import javax.annotation.Nullable;
import java.util.*;

public class Node {
    protected String sign;
    protected TokenType type;

    protected Set<Node> nextNodes = new LinkedHashSet<>();
    protected Node prevNode = null;

    public Node(TokenType type, String sign) {
        this.sign = sign;
        this.type = type;
    }

    public Node(TokenType type, String sign, Node... listNode) {
        this.sign = sign;
        this.type = type;
        for (Node node : listNode) {
            addNextNode(node);
        }
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Node(TokenType type, String sign, List<Node> listNode) {
        this.sign = sign;
        this.type = type;
        for (Node node : listNode) {
            addNextNode(node);
        }
    }

    public TokenType getType() {
        return type;
    }

    public Set<Node> getNextNodes() {
        return Collections.unmodifiableSet(nextNodes);
    }

    public void addNextNode(Node nextNode) {
        if(Objects.isNull(nextNode)) return;
        nextNode.prevNode = this;
        nextNodes.add(nextNode);
    }

    public void removeNextNode(Node nextNode) {
        if(nextNodes.contains(nextNode)) {
            nextNodes.remove(nextNode);
            nextNode.setPrevNode(null);
        }
    }

    public Node getPrevNode() {
        return prevNode;
    }


    public void setPrevNode(@Nullable Node prevNode) {
        if(Objects.isNull(prevNode)) {
            this.prevNode = null;
            return;
        }
        addNextNode(prevNode);
        this.prevNode = prevNode;
    }

    @Override
    public String toString() {
        return "Node{\n" + "sign='" + sign + "'\n" +
                "type=" + type + "\n" +
                "nextNodes=" + String.join("\n", Arrays.stream(nextNodes.toString().split("\n")).map(s -> "   " + s).toList()) + "\n" +
                "}\n";
    }
}
