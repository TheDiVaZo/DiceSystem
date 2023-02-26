package thedivazo.parserexpression;

import com.mojang.datafixers.types.Func;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.parserexpression.interpreter.Interpreter;
import thedivazo.parserexpression.lexer.Lexer;
import thedivazo.parserexpression.lexer.Token;
import thedivazo.parserexpression.lexer.TokenType;
import thedivazo.parserexpression.parser.Node;
import thedivazo.parserexpression.parser.OperatorType;
import thedivazo.parserexpression.parser.Parser;
import thedivazo.utils.TernaryOperator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ParserExpression<T, R> {
    private final Lexer lexer = new Lexer();
    private final Parser parser = new Parser();
    private final Interpreter<T, R> interpreter = new Interpreter<>();

    public interface TernaryOperatorWrapper<V> {
        String getSignOne();
        String getSignTwo();
        TernaryOperator<V> getTernaryOperator();
    }

    public interface BinaryOperatorWrapper<V> {
        String getSign();
        BinaryOperator<V> getBinaryOperator();
    }
    public interface UnaryOperatorWrapper<V> {
        String getSign();
        UnaryOperator<V> getUnaryOperator();
    }

    public void addTernaryOperator(TernaryOperatorWrapper<R> operatorData) {
        parser.addOperator(new Parser.OperatorData(operatorData.getSignOne(), OperatorType.TERNARY_1), new Parser.OperatorData(operatorData.getSignTwo(), OperatorType.TERNARY_2));
        lexer.putOperator(Pattern.quote(operatorData.getSignOne()),TokenType.OPERATOR);
        lexer.putOperator(Pattern.quote(operatorData.getSignTwo()),TokenType.OPERATOR);
        interpreter.addTernaryOperator(operatorData.getSignOne(), operatorData.getSignTwo(), operatorData.getTernaryOperator());
    }

    @SafeVarargs
    public final void addBinaryOperator(BinaryOperatorWrapper<R>... operatorsData) {
        parser.addOperator(
                Arrays
                        .stream(operatorsData)
                        .map(binaryOperatorWrapper -> new Parser.OperatorData(binaryOperatorWrapper.getSign(), OperatorType.BINARY))
                        .toList()
                        .toArray(new Parser.OperatorData[]{}));
        for (BinaryOperatorWrapper<R> operatorData : operatorsData) {
            lexer.putOperator(Pattern.quote(operatorData.getSign()), TokenType.OPERATOR);
            interpreter.addBinaryOperator(operatorData.getSign(),operatorData.getBinaryOperator());
        }
    }

    @SafeVarargs
    public final void addUnaryOperator(UnaryOperatorWrapper<R>... operatorsData) {
        parser.addOperator(
                Arrays
                        .stream(operatorsData)
                        .map(unaryOperatorWrapper -> new Parser.OperatorData(unaryOperatorWrapper.getSign(), OperatorType.UNARY))
                        .toList()
                        .toArray(new Parser.OperatorData[]{}));
        for (UnaryOperatorWrapper<R> operatorData : operatorsData) {
            lexer.putOperator(Pattern.quote(operatorData.getSign()), TokenType.OPERATOR);
            interpreter.addUnaryOperator(operatorData.getSign(),operatorData.getUnaryOperator());
        }
    }

    public void addCondition(String sign) {
        lexer.putOperator(sign, TokenType.CONDITION);
    }

    public void addCondition(String sign, Function<T,R> condition) {
        lexer.putOperator(sign, TokenType.CONDITION);
        interpreter.addCondition(sign, condition);
    }

    public void addFunction(String sign, Function<List<R>,R> function, Function<Integer, Boolean> argumentCompare) {
        lexer.putOperator(sign, TokenType.FUNCTION);
        parser.addNumberFunctionArgument(sign, argumentCompare);
        interpreter.addFunctionOperator(sign, function);
    }

    public void addCompoundOperators(String compoundStartSign, String compoundEndSign) {
        lexer.putOperator(compoundStartSign, TokenType.COMPOUND_START);
        lexer.putOperator(compoundEndSign, TokenType.COMPOUND_END);
    }

    public void addDelimiter(String delimiter) {
        lexer.putOperator(delimiter, TokenType.DELIMITER);
    }

    public void addSkipSymbols(String... skipSymbols) {
        for (String skipSymbol : skipSymbols) {
            lexer.putOperator(skipSymbol, TokenType.SPACE);
        }
    }

    public void setAlternativeConditionParser(Function<String, R> alternativeConditionParser) {
        interpreter.setAlternativeConditionParser(alternativeConditionParser);
    }

    public R execute(String code, T input) throws CompileException, InterpreterException {
        return interpreter.execute(parser.parsing(lexer.analyze(code)),input);
    }

    public R execute(Serializable objectNode, T input) throws InterpreterException {
        if(!(objectNode instanceof Node nodeMain)) throw new IllegalArgumentException("This object is not a code to be executed");
        return interpreter.execute(nodeMain, input);
    }

    public R execute(String code) throws CompileException, InterpreterException {
        return interpreter.execute(parser.parsing(lexer.analyze(code)),null);
    }

    public R execute(Serializable objectNode) throws InterpreterException {
        if(!(objectNode instanceof Node nodeMain)) throw new IllegalArgumentException("This object is not a code to be executed");
        return interpreter.execute(nodeMain, null);
    }

    public Serializable compile(String code) throws CompileException {
        return parser.parsing(lexer.analyze(code));
    }
}
