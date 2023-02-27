package thedivazo.dice;

import lombok.Setter;
import org.bukkit.entity.Player;
import thedivazo.DiceSystem;
import thedivazo.config.ConfigManager;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;

import java.lang.constant.Constable;
import java.util.*;

public class DiceManager {

    ParserExpression<Player, Constable> parserExpression;

    Map<String, CodeInText<Player, Constable>> variables = new HashMap<>();
    Map<String, Dice> dices = new HashMap<>();

    public DiceManager(){}

    Map<String, Dice> diceTypesMap = new HashMap<>();

    //TODO: удалить метод и создать вместо него throwDice. Дайсам ты будешь довать локальные переменные, в котором и будет заключаться доступ к другим дайсам.
    public Dice getDice(String dice) {
        return diceTypesMap.get(dice);
    }

    public void reload(ConfigManager configManager) throws CompileException {
        parserExpression = DiceSystem.getInstance().getParserExpression();
        updateVariables(configManager);
        updateDices(configManager);
    }

    public void updateVariables(ConfigManager configManager) throws CompileException {
        variables.clear();
        for (Map.Entry<String, String> variableEntry : configManager.getConditions().entrySet()) {
            CodeInText<Player, Constable> variable = new CodeInText<>(variableEntry.getValue(), parserExpression);
            variables.put(variableEntry.getKey(), variable);
        }
    }
    public void updateDices(ConfigManager configManager) throws CompileException {
        dices.clear();
        for (Map.Entry<String, ConfigManager.DiceType> diceTypeEntry : configManager.getDiceTypes().entrySet()) {
            ConfigManager.DiceType diceType = diceTypeEntry.getValue();
            Dice dice = new Dice(
                    diceTypeEntry.getKey(),
                    new CodeInText(diceType.getText(), parserExpression),
                    diceType.getPermission(),
                    parserExpression.compile(diceType.getDiceCode()),
                    parserExpression);
            dices.put(diceTypeEntry.getKey(), dice);
        }

    }


}
