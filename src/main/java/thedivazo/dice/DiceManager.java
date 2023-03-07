package thedivazo.dice;

import api.logging.Logger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import thedivazo.config.ConfigManager;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import java.lang.constant.Constable;
import java.util.*;

@NoArgsConstructor
public class DiceManager<T> {

    /*
        $player.getPotionEffect(1,2,3,4).getType()
     */

    ParserExpression<T, Constable, Object> parserExpression;

    Map<String, CodeInText<T, Constable, Object>> variables = new HashMap<>();
    Map<String, Dice<T, Constable, Object>> dices = new HashMap<>();

    public String throwDice(String diceName, T player) throws InterpreterException, CompileException {
        Dice<T, Constable, Object> dice = dices.get(diceName);
        Map<String, Object> localArguments = generateLocalArgument(player);
        localArguments.put("$dice", dice.throwDice(player, localArguments));
        return dice.diceText(player, localArguments);
    }

    protected Map<String, Object> generateLocalArgument(T player) throws InterpreterException, CompileException {
        Map<String, Object> localArguments = new HashMap<>();
        Logger.debug("Local argument generating...");
        for (Map.Entry<String,CodeInText<T, Constable, Object>> variablesEntry : variables.entrySet()) {
            localArguments.put(variablesEntry.getKey(), variablesEntry.getValue().getText(player, localArguments));
            Logger.debug("Local argument \"{0}\" has been generated", variablesEntry.getKey());
        }
        return localArguments;
    }

    public void reload(ConfigManager configManager, ParserExpression<T, Constable, Object> parserExpression) throws CompileException {
        Logger.debug("DiceManager reloading...");
        this.parserExpression = parserExpression;
        parserExpression.setCondition("\\$[a-zA-Z_\\-0-9\\]+");
        updateVariables(configManager);
        updateDices(configManager);
        Logger.debug("DiceManager has been reloaded");
    }

    public void updateVariables(ConfigManager configManager) throws CompileException {
        variables.clear();
        Logger.debug("Variable creating...");
        for (Map.Entry<String, String> variableEntry : configManager.getVariables().entrySet()) {
            CodeInText<T, Constable, Object> variable = new CodeInText<>(variableEntry.getValue(), parserExpression);
            variables.put("$"+variableEntry.getKey(), variable);
            Logger.debug("Variable \"{0}\" has been created","$"+variableEntry.getKey());
        }
    }
    public void updateDices(ConfigManager configManager) throws CompileException {
        dices.clear();
        Logger.debug("Dice creating...");
        for (Map.Entry<String, ConfigManager.DiceType> diceTypeEntry : configManager.getDiceTypes().entrySet()) {
            ConfigManager.DiceType diceType = diceTypeEntry.getValue();
            Dice<T, Constable, Object> dice = new Dice<>(
                    diceTypeEntry.getKey(),
                    new CodeInText(diceType.getText(), parserExpression),
                    diceType.getPermission(),
                    parserExpression.compile(diceType.getDiceCode()),
                    parserExpression);
            dices.put(diceTypeEntry.getKey(), dice);
            Logger.debug("Dice \"{0}\" has been created", diceTypeEntry.getKey());
        }

    }


}
