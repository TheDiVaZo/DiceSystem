package thedivazo.conditionhandler.parser;

import java.util.LinkedHashSet;
import java.util.Set;

public class Parser {



    /**
     * Типы операторов, расположение которых во множестве является их приоритетом.
     */
    protected Set<String> operatorsSet = new LinkedHashSet<>();


    public void addOperator(String operator) {
        operatorsSet.add(operator);
    }

    public void removeOperator(String operator) {
        operatorsSet.remove(operator);
    }

//    public MainNode parse(List<Token> tokenList) {
//
//    }




}
