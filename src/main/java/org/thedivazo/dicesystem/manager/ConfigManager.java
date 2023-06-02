package org.thedivazo.dicesystem.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.thedivazo.dicesystem.config.ConfigWrapper;

@NoArgsConstructor
public class ConfigManager {
    private double radius = 10.4;
    private String formatThrower = "You throw dice {roll}";
    private String formatSeer = "Player {thrower_name} throw dice {roll}";
    private String noPermission = "You have not permission to use this dice";
    private String diceNotFound = "Dice has not valid!";

    public void loadConfig(ConfigWrapper configWrapper) {
        radius = configWrapper.getDouble("settings.message.radius", radius);
        formatThrower = configWrapper.getString("settings.message.format-thrower", formatThrower);
        formatSeer = configWrapper.getString("settings.message.format-seer", formatSeer);
        noPermission = configWrapper.getString("settings.message.no-permission", noPermission);
        diceNotFound = configWrapper.getString("settings.message.dice-not-found", diceNotFound);
    }
}
