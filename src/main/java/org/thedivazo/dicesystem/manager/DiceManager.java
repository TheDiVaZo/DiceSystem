package org.thedivazo.dicesystem.manager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.thedivazo.dicesystem.config.ConfigWrapper;
import org.thedivazo.dicesystem.dice.Dice;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;

import java.util.Set;

public interface DiceManager {
    void loadDices(ConfigWrapper configWrapper) throws InvalidConfigurationException, WeightArgumentException;
    Dice<?> getDice(String diceTitle);
    boolean isDice(String diceTitle);

    Set<String> getDiceTitle();
}
