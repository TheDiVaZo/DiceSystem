package modules.logicconditionparser;

import lombok.NoArgsConstructor;
import modules.logicconditionparser.exception.*;
import modules.logicconditionparser.interpreter.*;
import modules.logicconditionparser.lexer.Lexer;
import modules.logicconditionparser.lexer.TokenType;
import modules.logicconditionparser.parser.Node;
import modules.logicconditionparser.parser.Parser;

@NoArgsConstructor
public class LogicConditionParser {
    Node AST = null;
    Interpreter interpreter = new Interpreter();
    Lexer lexer = new Lexer();

    public void compileCode(String code) throws ParsingException {
        AST = Parser.parseToAST(lexer.tokenize(code));
    }

    public boolean execute() throws ExecuteException {
        return interpreter.execute(AST);
    }

    public void setCondition(String condName, Condition condition) {
        interpreter.addCondition(condName, condition);
    }
    public Condition removeCondition(String condName) {
        return interpreter.removeCondition(condName);
    }

    public void addDefaultOperators() {
        lexer.addToken("!=", TokenType.BINARY_OPERATOR);
        lexer.addToken("==", TokenType.BINARY_OPERATOR);
        lexer.addToken("->", TokenType.BINARY_OPERATOR);
        lexer.addToken("^", TokenType.BINARY_OPERATOR);
        lexer.addToken("||", TokenType.BINARY_OPERATOR);
        lexer.addToken("&&", TokenType.BINARY_OPERATOR);
        lexer.addToken("!", TokenType.UNARY_OPERATOR);

        lexer.addToken("(", TokenType.COMPOUND_OPERATOR_START);
        lexer.addToken(")", TokenType.COMPOUND_OPERATOR_END);

        interpreter.addOperator("!=", (BooleanBinaryOperator) (cond1, cond2) -> cond1 != cond2);
        interpreter.addOperator("==", (BooleanBinaryOperator) (cond1, cond2) -> cond1 == cond2);
        interpreter.addOperator("^", (BooleanBinaryOperator) (cond1, cond2) -> cond1 ^ cond2);
        interpreter.addOperator("->", (BooleanBinaryOperator) (cond1, cond2) -> !cond1 || cond2);
        interpreter.addOperator("||", (BooleanBinaryOperator) (cond1, cond2) -> cond1 || cond2);
        interpreter.addOperator("&&", (BooleanBinaryOperator) (cond1, cond2) -> cond1 && cond2);
        interpreter.addOperator("!", (BooleanUnaryOperator) cond -> !cond);
    }

    public void addOperator(String operatorName, EmptyBooleanOperator operator) {
        lexer.addToken(operatorName, operator.getTokenType());
        interpreter.addOperator(operatorName, operator);
    }

    public void removeOperator(String operatorName) {
        lexer.removeToken(operatorName);
        interpreter.removeOperator(operatorName);
    }


}
