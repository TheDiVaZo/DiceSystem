package thedivazo.dice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public final class Dice<T, R extends B, B> {

    private final String name;
    private final CodeInText text;
    private final String permission;
    private final Serializable dice;

    private final ParserExpression<T, R, B> parserExpression;

    public B throwDice(T player, Map<String, B> localArguments) throws InterpreterException {
        return parserExpression.execute(dice, player, localArguments);
    }

    public String diceText(T player, Map<String, B> localArguments) throws InterpreterException, CompileException {
        B diceNumber = parserExpression.execute(dice, player);
        if(Objects.isNull(localArguments)) localArguments = new HashMap<>();
        localArguments.put("dice", diceNumber);
        return text.getText(player, localArguments);
    }


}
