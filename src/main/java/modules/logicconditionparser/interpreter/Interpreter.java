package modules.logicconditionparser.interpreter;

import lombok.NoArgsConstructor;
import modules.logicconditionparser.exception.*;
import modules.logicconditionparser.lexer.TokenType;
import modules.logicconditionparser.parser.Node;

import java.util.*;

@NoArgsConstructor
public class Interpreter {

    protected Map<String, EmptyBooleanOperator> operatorsMap = new HashMap<>();
    protected Map<String, Condition> conditionMap = new HashMap<>();

    public void addOperator(String nameOperation, EmptyBooleanOperator operation) {
        operatorsMap.put(nameOperation, operation);
    }

    public EmptyBooleanOperator removeOperator(String nameOperation) {
        return operatorsMap.remove(nameOperation);
    }

    public Map<String, EmptyBooleanOperator> getOperatorsMap() {
        return Collections.unmodifiableMap(operatorsMap);
    }

    public void addCondition(String nameCondition, Condition condition) {
        conditionMap.put(nameCondition, condition);
    }

    public Condition removeCondition(String nameCondition) {
        return conditionMap.remove(nameCondition);
    }

    public Map<String, Condition> getConditionMap() {
        return Collections.unmodifiableMap(conditionMap);
    }

    public boolean execute(Node node) throws ExecuteException {
        if(Objects.isNull(node)) throw new IllegalTokenException("Check the program for syntax errors! Please write the program as specified in the documentation.");
        else if(node.getType().equals(TokenType.CONDITION_NAME)) {
            Condition condition = getConditionMap().getOrDefault(node.getSign(), null);
            if(Objects.isNull(condition)) throw new IllegalConditionException("Condition \""+node.getSign()+"\" was not found.");
            return condition.getResult();
        }
        else if(node.getType().equals(TokenType.BINARY_OPERATOR)) {
            List<Node> argumentNode = node.getNextNodes().stream().toList();
            BooleanBinaryOperator callbackBinaryOperation = (BooleanBinaryOperator) getOperatorsMap().getOrDefault(node.getSign(), null);

            if(Objects.isNull(callbackBinaryOperation)) throw new IllegalConditionException("Binary operator \""+node.getSign()+"\" was not found.");
            if(argumentNode.size() != 2) throw new IllegalArgumentNumberException("The number of arguments for a binary operator \""+node.getSign()+"\" should be exactly 2. You have specified "+argumentNode.size()+" argument");

            return callbackBinaryOperation.apply(execute(argumentNode.get(0)), execute(argumentNode.get(1)));
        }
        else if(node.getType().equals(TokenType.UNARY_OPERATOR)) {
            List<Node> argumentNode = node.getNextNodes().stream().toList();
            BooleanUnaryOperator callbackUnaryOperator = (BooleanUnaryOperator) getOperatorsMap().getOrDefault(node.getSign(), null);

            if(Objects.isNull(callbackUnaryOperator)) throw new IllegalConditionException("Unary operator \""+node.getSign()+"\" was not found.");
            if(argumentNode.size() != 1) throw new IllegalArgumentNumberException("The number of arguments for a unary operator \""+node.getSign()+"\" should be exactly 1. You have specified "+argumentNode.size()+" argument");

            return callbackUnaryOperator.apply(execute(argumentNode.get(0)));
        }
        else throw new UnknownOperatorException("Node type \""+node.getType()+"\" was not found");
    }


}
