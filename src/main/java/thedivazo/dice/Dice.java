package thedivazo.dice;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class Dice {

    private final String name;
    private final CodeInText text;
    private final String permission;
    private final Serializable dice;

    private final ParserExpression<Player, Constable> parserExpression;

    public Double throwDice(Player player) throws InterpreterException {
        return (Double) parserExpression.execute(dice, player);
    }

    public String diceText(Player player) throws InterpreterException, CompileException {
        Double diceNumber = (Double) parserExpression.execute(dice, player);
        Map<String, Object> localCondition = new HashMap<>();
        localCondition.put("dice", diceNumber);
        return text.getText(player, localCondition);
    }


}
