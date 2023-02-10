package thedivazo.conditionhandler.parser;

import thedivazo.conditionhandler.parser.AST.BinaryOperatorNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;

import java.util.Set;

public class TestOperatorClass extends BinaryOperatorNode {
    public TestOperatorClass(String nodeName) {
        super(nodeName);
    }

    @Override
    public boolean setNodes(Set<Node> nodes) {
        if(nodes.stream().anyMatch(node -> node instanceof ConditionNode)) throw new RuntimeException("No condition argument");
        return super.setNodes(nodes);
    }
}
