package org.thedivazo.dicesystem.manager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.thedivazo.dicesystem.config.ConfigWrapper;
import org.thedivazo.dicesystem.dice.DefaultDice;
import org.thedivazo.dicesystem.dice.Dice;
import org.thedivazo.dicesystem.dice.Side;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;
import org.thedivazo.dicesystem.logging.Logger;

import java.util.*;

public class DefaultDiceManager implements DiceManager {
    private final Map<String, Dice<?>> diceMap = new HashMap<>();

    public void loadDices(ConfigWrapper configWrapper) throws InvalidConfigurationException, WeightArgumentException {
        Logger.info("Loading dice from config...");
        diceMap.clear();
        ConfigWrapper dicesSection = configWrapper.getRequiredConfigurationSection("dices");
        Set<String> diceTitles = dicesSection.getKeys(false);
        for (String diceTitle : diceTitles) {
            ConfigWrapper diceSection = dicesSection.getRequiredConfigurationSection(diceTitle);
            String diceName = diceSection.getRequiredString("name");
            List<String> diceSides = diceSection.getRequireStringList("sides");
            Map<String, Double> weights = diceSection.getMap("prob-weight", Double.class, Collections.emptyMap());
            Dice.DiceBuilder<String> diceBuilder = DefaultDice.builder(diceName);
            for (String diceSide : diceSides) {
                diceBuilder.addSide(new Side<>(diceSide, weights.getOrDefault(diceSide, 1d)));
            }
            diceMap.put(diceTitle, diceBuilder.build());
            Logger.info("Dice '"+diceName+"' ("+diceTitle+") successfully uploaded!");
        }
        Logger.info("All dice have been loaded");
    }

    public Dice<?> getDice(String diceTitle) {
        return diceMap.get(diceTitle);
    }

    public boolean isDice(String diceTitle) {
        return diceMap.containsKey(diceTitle);
    }

    @Override
    public Set<String> getDiceTitle() {
        return diceMap.keySet();
    }
}
