package thedivazo.dice;

import api.logging.Logger;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import thedivazo.config.ConfigManager;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.wrapper.PlayerWrapper;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.util.*;

@NoArgsConstructor
public class DiceManager {

    /*
        $player.getPotionEffect(1,2,3,4).getType()
     */

    ParserExpression<Player, Constable, Constable> parserExpression;

    Map<String, Serializable> variables = new HashMap<>();
    Map<String, Dice<Player, Constable, Constable>> dices = new HashMap<>();

    public Dice.ThrewDiceObject<Constable> throwDice(String diceName, Player player) throws InterpreterException, CompileException {
        if(Objects.isNull(dices.get(diceName))) throw new CompileException(String.format("Variable \"%s\" not find", diceName));
        return dices.get(diceName).throwDice(player, generateLocalArguments(player));
    }

    protected Map<String, Constable> generateLocalArguments(Player player) throws InterpreterException {
        Map<String, Constable> localArguments = new HashMap<>();
        localArguments.put("player", new PlayerWrapper(player));
        for (Iterator<Map.Entry<String, Serializable>> iterator = variables.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Serializable> variableEntry = iterator.next();
            localArguments.put(variableEntry.getKey(), parserExpression.execute(variableEntry.getValue(), player, localArguments));
        }
        return localArguments;
    }

    public void reload(ConfigManager configManager, ParserExpression<Player, Constable, Constable> parserExpression) throws CompileException {
        Logger.debug("DiceManager reloading...");
        this.parserExpression = parserExpression;
        updateVariables(configManager);
        updateDices(configManager);
        Logger.debug("DiceManager has been reloaded");
    }

    public void updateVariables(ConfigManager configManager) throws CompileException {
        variables.clear();
        for (Map.Entry<String, String> variableEntry : configManager.getVariables().entrySet()) {
            String nameVariable = variableEntry.getKey();
            Serializable code = parserExpression.compile(variableEntry.getValue());
            variables.put(nameVariable, code);
        }

    }
    public void updateDices(ConfigManager configManager) throws CompileException {
        dices.clear();
        for (Map.Entry<String, ConfigManager.DiceType> variableEntry : configManager.getDiceTypes().entrySet()) {
            String nameDice = variableEntry.getKey();
            ConfigManager.DiceType diceType = variableEntry.getValue();
            Dice<Player, Constable, Constable> dice = new Dice<>(
                    nameDice,
                    new CodeInText<>(diceType.getText(), parserExpression),
                    diceType.getPermission(),
                    parserExpression.compile(diceType.getDiceCode()),
                    parserExpression);
            dices.put(nameDice, dice);
        }


    }


}
