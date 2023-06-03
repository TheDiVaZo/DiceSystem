package org.thedivazo.dicesystem.manager.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.thedivazo.dicesystem.config.ConfigWrapper;
import org.thedivazo.dicesystem.dice.DefaultDice;
import org.thedivazo.dicesystem.dice.Dice;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;
import org.thedivazo.dicesystem.manager.dice.DefaultDiceManager;
import org.thedivazo.dicesystem.manager.dice.DiceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class ConfigManager {
    private double radius = 10.4;
    private String formatThrower = "You throw dice {roll}";
    private String formatSeer = "Player {thrower_name} throw dice {roll}";
    private String noPermission = "You have not permission to use this dice";
    private String diceNotFound = "Dice has not valid!";

    @Getter
    @Setter
    @NotNull
    private DiceManager diceManager = new DefaultDiceManager();

    public void loadConfig(ConfigWrapper configWrapper) throws WeightArgumentException, InvalidConfigurationException {
        radius = configWrapper.getDouble("settings.message.radius", radius);
        formatThrower = configWrapper.getString("settings.message.format-thrower", formatThrower);
        formatSeer = configWrapper.getString("settings.message.format-seer", formatSeer);
        noPermission = configWrapper.getString("settings.message.no-permission", noPermission);
        diceNotFound = configWrapper.getString("settings.message.dice-not-found", diceNotFound);
        diceManager.loadDices(configWrapper);
    }


}
