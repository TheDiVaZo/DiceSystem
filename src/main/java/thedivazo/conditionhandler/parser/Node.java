package thedivazo.conditionhandler.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public abstract class Node {
    @Getter
    protected final String nodeName;

    /**
     * Устанавливает дочерние узлы.
     * @param nodes множество узлов.
     * @return Возвращает статус установки узла. Если все узлы установлены, возвращает true
     */
    public abstract boolean setNodes(Node... nodes);


    /**
     * @return Возвращает неизменяемый список узлов, в котором гарантированно нет одинаковых элементов.
     */
    public abstract Set<Node> getChildrenNodes();

    @Override
    public String toString() {
        return nodeName;
    }
}
