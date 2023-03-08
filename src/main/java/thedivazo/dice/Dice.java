package thedivazo.dice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import java.io.Serializable;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public final class Dice<T, R extends B, B> {

    private final String name;
    private final CodeInText<T, R, B> message;
    private final String permission;
    private final Serializable dice;

    private final ParserExpression<T, R, B> parserExpression;

    public ThrewDiceObject<B> throwDice(T player, Map<String, B> localArguments) throws InterpreterException, CompileException {
        B result = parserExpression.execute(dice, player, localArguments);
        localArguments.put("dice", result);
        String messageText = getMessage().getText(player, localArguments);
        return new ThrewDiceObject<>(result, messageText);
    }

    public record ThrewDiceObject<B>(B result, String message){}


}
