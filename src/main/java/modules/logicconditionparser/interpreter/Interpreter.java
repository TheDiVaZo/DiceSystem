package modules.logicconditionparser.interpreter;

import lombok.NoArgsConstructor;
import modules.logicconditionparser.exception.IllegalArgumentNumberException;
import modules.logicconditionparser.exception.IllegalConditionException;
import modules.logicconditionparser.exception.IllegalTokenException;
import modules.logicconditionparser.exception.UnknownOperatorException;
import modules.logicconditionparser.lexer.TokenType;
import modules.logicconditionparser.parser.Node;

import java.util.*;

@NoArgsConstructor
public class Interpreter {
    protected interface CallbackOperator {}
    protected interface CallbackBinaryOperator extends CallbackOperator {
        boolean exclude(boolean cond1, boolean cond2);
    }
    protected interface CallbackUnaryOperator extends CallbackOperator {
        boolean exclude(boolean cond1);
    }

    protected interface Condition {
        boolean getResult();
    }
    protected Map<String, CallbackOperator> operatorsMap = new HashMap<>();
    protected Map<String, Condition> conditionMap = new HashMap<>();

    public void addOperator(String nameOperation, CallbackOperator operation) {
        operatorsMap.put(nameOperation, operation);
    }

    public CallbackOperator removeOperator(String nameOperation) {
        return operatorsMap.remove(nameOperation);
    }

    public Map<String, CallbackOperator> getOperatorsMap() {
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

    public boolean execute(Node node) throws IllegalArgumentNumberException, IllegalConditionException, UnknownOperatorException, IllegalTokenException {
        if(Objects.isNull(node)) throw new IllegalTokenException("Check the program for syntax errors! Please write the program as specified in the documentation.");
        else if(node.getType().equals(TokenType.CONDITION_NAME)) {
            Condition condition = getConditionMap().getOrDefault(node.getSign(), null);
            if(Objects.isNull(condition)) throw new IllegalConditionException("Condition \""+node.getSign()+"\" was not found.");
            return condition.getResult();
        }
        else if(node.getType().equals(TokenType.BINARY_OPERATOR)) {
            List<Node> argumentNode = node.getNextNodes().stream().toList();
            if(argumentNode.size() != 2) throw new IllegalArgumentNumberException("The number of arguments for a binary operator should be exactly 2. You have specified "+argumentNode.size()+" argument");
            CallbackBinaryOperator callbackBinaryOperation = (CallbackBinaryOperator) getOperatorsMap().getOrDefault(node.getSign(), null);
            if(Objects.isNull(callbackBinaryOperation)) throw new IllegalConditionException("Binary operator \""+node.getSign()+"\" was not found.");
            return callbackBinaryOperation.exclude(execute(argumentNode.get(0)), execute(argumentNode.get(1)));
        }
        else if(node.getType().equals(TokenType.UNARY_OPERATOR)) {
            List<Node> argumentNode = node.getNextNodes().stream().toList();
            if(argumentNode.size() != 1) throw new IllegalArgumentNumberException("The number of arguments for a unary operator should be exactly 1. You have specified "+argumentNode.size()+" argument");
            CallbackUnaryOperator callbackUnaryOperator = (CallbackUnaryOperator) getOperatorsMap().getOrDefault(node.getSign(), null);
            if(Objects.isNull(callbackUnaryOperator)) throw new IllegalConditionException("Unary operator \""+node.getSign()+"\" was not found.");
            return callbackUnaryOperator.exclude(execute(argumentNode.get(0)));
        }
        else throw new UnknownOperatorException("Node type \""+node.getType()+"\" was not found");
    }


}
