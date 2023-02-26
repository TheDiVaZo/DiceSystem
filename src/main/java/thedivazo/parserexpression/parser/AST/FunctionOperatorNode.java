package thedivazo.parserexpression.parser.AST;

import thedivazo.parserexpression.parser.Node;

import java.util.List;
import java.util.function.Function;

public class FunctionOperatorNode extends OperatorNode {
    public FunctionOperatorNode(String nodeName) {
        super(nodeName);
    }

    @Override
    public boolean setNodes(Node... nodes) {
        return super.setNodes(nodes);
    }
}
