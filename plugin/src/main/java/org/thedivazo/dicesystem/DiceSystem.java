package org.thedivazo.dicesystem;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DiceSystem extends JavaPlugin {
    private final Logger logger = getLogger();
    private final String VERSION = DiceSystem.class.getPackage().getImplementationVersion();
    private final String NAME = "DiceSystem";

    @Override
    public void onEnable() {
        super.onEnable();
        logger.info("DiceSystem ");
    }
}