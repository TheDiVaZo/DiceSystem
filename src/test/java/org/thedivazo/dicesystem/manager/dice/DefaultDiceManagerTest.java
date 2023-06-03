package org.thedivazo.dicesystem.manager.dice;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.thedivazo.dicesystem.config.ConfigWrapper;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;
import org.thedivazo.dicesystem.logging.Logger;
import org.thedivazo.dicesystem.logging.handlers.JULHandler;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDiceManagerTest {

    @Test
    void loadDices() throws IOException, WeightArgumentException, InvalidConfigurationException {
        Logger.init(new JULHandler(java.util.logging.Logger.getGlobal()));
        DefaultDiceManager defaultDiceManager = new DefaultDiceManager();
        ConfigurationSection configurationSection = YamlConfiguration.loadConfiguration(Files.newBufferedReader(Paths.get("src/test/resources/config.yml")));
        ConfigWrapper configWrapper = new ConfigWrapper(configurationSection);

        defaultDiceManager.loadDices(configWrapper);;
    }
}