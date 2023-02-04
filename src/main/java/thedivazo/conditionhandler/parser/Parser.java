package thedivazo.conditionhandler.parser;

import thedivazo.conditionhandler.exception.SyntaxException;
import thedivazo.conditionhandler.lexer.Token;
import thedivazo.conditionhandler.lexer.TokenType;
import thedivazo.conditionhandler.parser.AST.BinaryOperationNode;
import thedivazo.conditionhandler.parser.AST.ConditionNode;
import thedivazo.conditionhandler.parser.AST.ExpressionNode;
import thedivazo.conditionhandler.parser.AST.UnaryOperationNode;

import java.util.*;

public class Parser {

    public Parser() {

    }

    protected List<FunctionParser<TokenBuffer,? extends ExpressionNode>> binaryOperatorPriorityParsers = new ArrayList<>();

    /**
     * Данный метод используется для парсинга всего буфера.
     * @param tokenBuffer Буффер токенов, который нужно пропарсить
     * @return Возвращает конечный узел, в котором содержится все выражение
     * @throws SyntaxException Генерируется исключение, если в буффере присутствуют синтаксические ошибки.
     */
    protected ExpressionNode mainParser(TokenBuffer tokenBuffer) throws SyntaxException {
        Token token = tokenBuffer.next();
        if(token.getLexemeType().equals(TokenType.EOF)) return new ConditionNode("");
        else {
            tokenBuffer.prev();
            return binaryOperatorPriorityParsers.get(binaryOperatorPriorityParsers.size()-1).applyAndException(tokenBuffer);
        }
    };

    public static String generateCodeFromToken(List<Token> tokenList) {
        return String.join("", tokenList.stream().map(Token::getSign).toList());
    }


    /**
     * Данный метод парсит только переменные и унарные операторы.
     * @param tokenBuffer Буффер с токенами
     * @return Возвращает переменную, либо объект унарного оператора
     * @throws SyntaxException Генерирует исключение, если в буффере есть синтаксические ошибки
     */
    protected ExpressionNode parseVariableAndUnary(TokenBuffer tokenBuffer) throws SyntaxException {
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
                    ExpressionNode conditionNode = mainParser(tokenBuffer);
                    token = tokenBuffer.next();
                    if (Objects.isNull(token)) {
                        Token prevToken = tokenBuffer.prev();
                        throw new SyntaxException("Expected end compound token", prevToken.getPosition()+prevToken.getSign().length(), generateCodeFromToken(tokenBuffer.getTokenList()));
                    }
                    else if (!token.getLexemeType().equals(TokenType.COMPOUND_END))
                        throw new SyntaxException(String.format("Unexpected token: %s",token.getSign()), token.getPosition(), generateCodeFromToken(tokenBuffer.getTokenList()));
                    return conditionNode;
                }
                case UNARY_OPERATION -> {
                    UnaryOperationNode unaryOperationNode = new UnaryOperationNode(token.getSign());
                    unaryOperationNode.addNextNode(parseVariableAndUnary(tokenBuffer));
                    return unaryOperationNode;
                }
                case SPACE -> {}
                default -> throw new SyntaxException(String.format("Unexpected token: %s",token.getSign()), token.getPosition(), generateCodeFromToken(tokenBuffer.getTokenList()));
            }
        }
    }


    /**
     * Пока что только можно устанавливать приоритет бинарных операторов.
     * Я еще не придумал, как реализовать систему приоритета для унарных.
     * Поэтому все унарные операторы на данный момент имеют приоритет, равный переменным.
     * Обратите внимание, что если не добавить в аргумент токен бинарного оператора, regEx которого есть в лексере, то парсер этот оператор не определит.
     * @param operators Массив текстовых токенов бинарных операторов, сортированных относительно их приоритета (операторы в начале массива приоритетнее операторов в конце)
     */
    public void binaryOperatorPriorityParserGenerator(String... operators) {
        for (int i = 0; i < operators.length; i++) {
            String operator = operators[i];
            int finalI = i;
            FunctionParser<TokenBuffer, ? extends ExpressionNode> functionParser = tokenBuffer -> {
                ExpressionNode value = finalI == 0 ? parseVariableAndUnary(tokenBuffer): binaryOperatorPriorityParsers.get(finalI -1).applyAndException(tokenBuffer);
                while (true) {
                    Token token = tokenBuffer.next();
                    if(Objects.isNull(token)) return value;
                    if (token.getLexemeType() == TokenType.SPACE) continue;
                    else if (token.getLexemeType() == TokenType.BINARY_OPERATION && token.getSign().equals(operator)) {
                        BinaryOperationNode binaryOperationNode = new BinaryOperationNode(operator);
                        binaryOperationNode.addNextNode(value);
                        binaryOperationNode.addNextNode(finalI == 0 ? parseVariableAndUnary(tokenBuffer): binaryOperatorPriorityParsers.get(finalI -1).applyAndException(tokenBuffer));

                        value = binaryOperationNode;
                    }// else if (token.getLexemeType() == TokenType.BINARY_OPERATION) throw new UnknownOperatorException(String.format("Unknown binary operation: %s",token.getSign()), token.getPosition(), generateCodeFromToken(tokenBuffer.getTokenList()));
                    else {
                        tokenBuffer.prev();
                        return value;
                    }
                }
            };
            binaryOperatorPriorityParsers.add(functionParser);
        }
    }

    /**
     * @param tokenList Массив токенов, из которых нужно сгенерировать AST.
     * @return Возвращает главный узел, который является вершиной AST
     * @throws SyntaxException Если в коде будут синтаксические ошибки, сгенерируется исключение
     */
    public ExpressionNode parse(List<Token> tokenList) throws SyntaxException {
        return mainParser(new TokenBuffer(tokenList));
    }

    /*
    RULES:
    !cond1
    !(cond1 || cond2)
     */




}
