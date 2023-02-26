package thedivazo.dice;

import lombok.Setter;
import org.bukkit.entity.Player;
import thedivazo.config.ConfigManager;
import thedivazo.parserexpression.ParserExpression;

import java.lang.constant.Constable;
import java.util.*;

public class DiceManager {

    @Setter
    ParserExpression<Player, Constable> parserExpression;

    public DiceManager(){}

    Map<String, Dice> diceTypesMap = new HashMap<>();

    public Dice getDice(String dice) {
        return diceTypesMap.get(dice);
    }

    public void reload(ConfigManager configManager) {
        //TODO writeMethod
    }


}
