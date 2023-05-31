package org.thedivazo.dicesystem;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.thedivazo.dicesystem.command.DefaultCommand;
import org.thedivazo.dicesystem.config.ConfigWrapper;
import org.thedivazo.dicesystem.dice.DefaultDice;
import org.thedivazo.dicesystem.dice.Dice;
import org.thedivazo.dicesystem.dice.Side;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;
import org.thedivazo.dicesystem.logging.Logger;
import org.thedivazo.dicesystem.logging.handlers.JULHandler;

import java.util.*;

public class DiceSystem extends JavaPlugin {
    Map<String, Dice<?>> diceMap = new HashMap<>();

    public Map<String, Dice<?>> getDiceMap() {
        return Collections.unmodifiableMap(diceMap);
    }
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
        try {
            loadConfig();
        } catch (InvalidConfigurationException | WeightArgumentException e) {
            Logger.error(e.getMessage(), e);
        }
    }


    public void reload() throws WeightArgumentException, InvalidConfigurationException {
        Logger.info("Plugin reload started...");
        reloadConfig();
        configWrapper = new ConfigWrapper(getConfig());
        loadConfig();
    }

    public void loadConfig() throws InvalidConfigurationException, WeightArgumentException {
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

    public void loadCommand() {
        manager = new PaperCommandManager(this);
        manager.getCommandCompletions().registerCompletion("diceTitles", c -> ImmutableList.copyOf(diceMap.keySet()));
        manager.registerDependency(DiceSystem.class, this);
        manager.registerCommand(new DefaultCommand());
    }

    @Override
    public void onDisable() {
        Logger.info("Disable plugin...");
        super.onDisable();
    }
}