package thedivazo.conditionhandler.parser.AST;

import thedivazo.conditionhandler.parser.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConditionNode extends Node {

    public ConditionNode(String nodeName) {
        super(nodeName);
    }


    /**
     * @param nodes множество узлов.
     * @return Всегда возвращает {@link UnsupportedOperationException}, так как узел условий хранит только свое имя, которые и является набором условий.
     */
    @Override
    public boolean setNodes(Node... nodes) {
        throw new UnsupportedOperationException("You cannot add nodes to a condition.");
    }

    /**
     * @return Всегда возвращает {@link UnsupportedOperationException}, так как узел условий хранит только свое имя, которые и является набором условий.
     */
    @Override
    public Set<Node> getChildrenNodes() {
        throw new UnsupportedOperationException("You cannot get nodes to a condition.");
    }
}
