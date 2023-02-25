package thedivazo.parserexpression.parser.AST;

import thedivazo.parserexpression.parser.Node;

public class FunctionOperatorNode extends OperatorNode {

    private int numberArgument;
    public FunctionOperatorNode(String nodeName, int numberArgument) {
        super(nodeName);
        this.numberArgument = numberArgument;
    }

    @Override
    public boolean setNodes(Node... nodes) {
        if(nodes.length!=numberArgument) throw new IllegalArgumentException(String.format("A function operator \"%s\" must have %s arguments.", nodeName, numberArgument));
        return super.setNodes(nodes);
    }
}
