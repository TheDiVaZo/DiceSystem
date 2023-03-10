package thedivazo.parserexpression.parser.AST;

import lombok.Getter;
import lombok.Setter;
import thedivazo.parserexpression.parser.Node;

public class MethodOperatorNode extends FunctionOperatorNode {

    @Getter
    @Setter
    protected Node context;

    public MethodOperatorNode(String nodeName) {
        super(nodeName);
    }

    @Override
    public String toString() {
        return super.toString() + "." + context.toString();
    }
}
