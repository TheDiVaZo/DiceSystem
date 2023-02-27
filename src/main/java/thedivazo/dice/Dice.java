package thedivazo.dice;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public final class Dice {

    private final String name;
    private final CodeInText text;
    private final String permission;
    private final Serializable dice;

    private final ParserExpression<Player, Constable> parserExpression;

    public Double throwDice(Player player, @Nullable Map<String, Constable> localArguments) throws InterpreterException {
        return (Double) parserExpression.execute(dice, player, localArguments);
    }

    public String diceText(Player player, @Nullable Map<String, Constable> localArguments) throws InterpreterException, CompileException {
        Double diceNumber = (Double) parserExpression.execute(dice, player);
        if(Objects.isNull(localArguments)) localArguments = new HashMap<>();
        localArguments.put("dice", diceNumber);
        return text.getText(player, localArguments);
    }


}
