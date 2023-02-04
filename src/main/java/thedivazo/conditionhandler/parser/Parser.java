package thedivazo.conditionhandler.parser;

import org.apache.commons.collections4.map.LinkedMap;
import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.exception.UnsupportedOperatorException;
import thedivazo.conditionhandler.lexer.Token;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.AST.BinaryOperationNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;
import thedivazo.conditionhandler.parser.AST.ExpressionNode;
import thedivazo.conditionhandler.parser.AST.UnaryOperationNode;

import java.util.*;
import java.util.function.Function;

public class Parser {

    public Parser() {
        operatorRulesMap.add(variableParser);
    }

    protected List<FunctionParser<TokenBuffer,? extends ExpressionNode>> operatorRulesMap = new ArrayList<>();

    protected FunctionParser<TokenBuffer, ? extends ExpressionNode> finalParser = tokenBuffer -> {
        Token token = tokenBuffer.next();
        if(token.getLexemeType().equals(TokenType.EOF)) return new ConditionNode("");
        else {
            tokenBuffer.prev();
            return operatorRulesMap.get(operatorRulesMap.size()-1).applyAndException(tokenBuffer);
        }
    };

    public static String generateCodeFromToken(List<Token> tokenList) {
        return String.join("", tokenList.stream().map(Token::getSign).toList());
    }

    /**
     * Парсер, который парсит ТОЛЬКО токены-переменные, пробелы и операторы приоритета.
     */
    protected FunctionParser<TokenBuffer, ? extends ExpressionNode> variableParser = tokenBuffer -> {
        while (true) {
            Token token = tokenBuffer.next();
            if(Objects.isNull(token)) {
                Token tokenPrev = tokenBuffer.prev();
                throw new SyntaxException("Variable expected.", tokenPrev.getPosition()+tokenPrev.getSign().length(), generateCodeFromToken(tokenBuffer.getTokenList()));
            }
            switch (token.getLexemeType()) {
                case CONDITION ->{return new ConditionNode(token.getSign());}
                case TRUE ->{return new ConditionNode("true");}
                case FALSE ->{return new ConditionNode("false");}
                case COMPOUND_START -> {
                    ExpressionNode conditionNode = finalParser.applyAndException(tokenBuffer);
                    token = tokenBuffer.next();
                    if (!token.getLexemeType().equals(TokenType.COMPOUND_END))
                        throw new SyntaxException(String.format("Unexpected token: %s",token.getSign()), token.getPosition(), generateCodeFromToken(tokenBuffer.getTokenList()));
                    return conditionNode;
                }
                case SPACE -> {}
                default -> throw new SyntaxException(String.format("Unexpected token: %s",token.getSign()), token.getPosition(), generateCodeFromToken(tokenBuffer.getTokenList()));
            }
        }
    };


    /**
     * @param operators Массив операторов, которые будут в токенах. Положение оператора в массиве является приоритетом этого оператора. Важное уточнение: В массив операторов, нельзя добавлять операторы, изменяющие приоритет (по типу круглых скобок) и операторы, представляющие переменные.
     * @throws UnsupportedOperatorException выкидывается, если в передоваемых операторов есть переменные или операторы изменения приоритета (круглые скобки например)
     */
    public void generateRules(String... operators) throws UnsupportedOperatorException {
        for (int i = 0; i < operators.length; i++) {
            String operator = operators[i];
            int finalI = i+1;
            FunctionParser<TokenBuffer, ? extends ExpressionNode> functionParser = tokenBuffer -> {
                ExpressionNode value = operatorRulesMap.get(finalI -1).applyAndException(tokenBuffer);
                while (true) {
                    Token token = tokenBuffer.next();
                    if(Objects.isNull(token)) return value;
                    if (token.getLexemeType() == TokenType.SPACE) continue;
                    else if (token.getLexemeType() == TokenType.BINARY_OPERATION && token.getSign().equals(operator)) {
                        BinaryOperationNode binaryOperationNode = new BinaryOperationNode(operator);
                        binaryOperationNode.addNextNode(value);
                        binaryOperationNode.addNextNode(operatorRulesMap.get(finalI - 1).applyAndException(tokenBuffer));

                        value = binaryOperationNode;
                    } else {
                        tokenBuffer.prev();
                        return value;
                    }
                }
            };
            operatorRulesMap.add(functionParser);
        }
    }

    /**
     * @param tokenList Массив токенов, из которых нужно сгенерировать синтаксическое дерево.
     * @return Возвращает главный узел, который является вершиной AST
     * @throws SyntaxException Если в коде будут синтаксические ошибки, сгенерируется исключение
     */
    public ExpressionNode parse(List<Token> tokenList) throws SyntaxException {
        return finalParser.applyAndException(new TokenBuffer(tokenList));
    }

    /*
    RULES:
    !cond1
    !(cond1 || cond2)
     */




}
