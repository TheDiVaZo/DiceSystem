package thedivazo.conditionhandler.parser.AST;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainNode extends ExpressionNode {

    protected Set<ExpressionNode> childrens = new LinkedHashSet<>();

    public MainNode() {
        super("MAIN");
    }

    @Override
    public void addNextNode(ExpressionNode node) {
        if(childrens.contains(node)) return;
        childrens.add(node);
        node.setPrevNode(this);
    }

    public Set<ExpressionNode> getChildrens() {
        return Collections.unmodifiableSet(childrens);
    }
}
