package org.thedivazo.dicesystem;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.thedivazo.dicesystem.command.DefaultCommand;
import org.thedivazo.dicesystem.config.ConfigWrapper;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;
import org.thedivazo.dicesystem.logging.Logger;
import org.thedivazo.dicesystem.logging.handlers.JULHandler;
import org.thedivazo.dicesystem.manager.ConfigManager;
import org.thedivazo.dicesystem.manager.DefaultDiceManager;
import org.thedivazo.dicesystem.manager.DiceManager;

@Plugin(
        name = "DiceSystem",
        version = "1.0.0"
)
@Author("TheDiVaZo")
@LogPrefix("DiceSystem")
@SoftDependency("PlaceholderAPI")
@ApiVersion(ApiVersion.Target.v1_16)
public class DiceSystem extends JavaPlugin {

    @Getter
    @Setter
    private DiceManager diceManager;

    private ConfigManager configManager;
    ConfigWrapper configWrapper;

    PaperCommandManager manager;

    private static DiceSystem instance;

    public static DiceSystem getInstance() {
        return instance;
    }

    {
        instance = this;
    }
    @Override
    public void onEnable() {
        super.onEnable();
        Logger.init(new JULHandler(getLogger()));
        saveDefaultConfig();
        configWrapper = new ConfigWrapper(getConfig());
        setDiceManager(new DefaultDiceManager());
        configManager = new ConfigManager();
        try {
            getDiceManager().loadDices(configWrapper);
            configManager.loadConfig(configWrapper);
        } catch (InvalidConfigurationException | WeightArgumentException e) {
            Logger.error(e.getMessage(), e);
        }
    }


    public void reload() throws WeightArgumentException, InvalidConfigurationException {
        Logger.info("Plugin reload started...");
        reloadConfig();
        configWrapper = new ConfigWrapper(getConfig());
        getDiceManager().loadDices(configWrapper);
        configManager.loadConfig(configWrapper);
    }

    public void loadCommand() {
        manager = new PaperCommandManager(this);
        manager.getCommandCompletions().registerCompletion("diceTitles", c -> ImmutableList.copyOf(getDiceManager().getDiceTitle()));
        manager.registerDependency(DiceSystem.class, this);
        manager.registerCommand(new DefaultCommand());
    }

    @Override
    public void onDisable() {
        Logger.info("Disable plugin...");
        super.onDisable();
    }
}