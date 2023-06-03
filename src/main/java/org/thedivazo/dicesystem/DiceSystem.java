package org.thedivazo.dicesystem;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
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
import org.thedivazo.dicesystem.manager.config.ConfigManager;
import org.thedivazo.dicesystem.manager.dice.DefaultDiceManager;

import java.util.Objects;

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
    private ConfigManager configManager;
    private PaperCommandManager commandManager;

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
        Logger.info("Enable plugin");

        saveDefaultConfig();
        try {
            loadConfig();
        } catch (InvalidConfigurationException | WeightArgumentException e) {
            Logger.error(e.getMessage(), e);
        }
        loadCommand();

        Logger.info("Plugin ready to go");
    }

    public void loadConfig() throws WeightArgumentException, InvalidConfigurationException {
        Logger.info("Config start load");

        ConfigWrapper configWrapper = new ConfigWrapper(getConfig());
        if (Objects.isNull(configManager)) configManager = new ConfigManager();
        configManager.loadConfig(configWrapper);

        Logger.info("Config loaded");
    }


    public void reload() throws WeightArgumentException, InvalidConfigurationException {
        Logger.info("Plugin reload started...");

        reloadConfig();
        loadConfig();
        loadCommand();

        Logger.info("Plugin reloaded");
    }

    public void loadCommand() {
        Logger.info("Commands start loading...");

        commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerCompletion("diceTitles", c -> ImmutableList.copyOf(getConfigManager().getDiceManager().getDiceTitles()));
        commandManager.registerDependency(ConfigManager.class, getConfigManager());
        commandManager.registerDependency(DiceSystem.class, this);
        commandManager.registerCommand(new DefaultCommand());

        Logger.info("Commands loaded");
    }

    @Override
    public void onDisable() {
        Logger.info("Disable plugin...");
        super.onDisable();
    }
}