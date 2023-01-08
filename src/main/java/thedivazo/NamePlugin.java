package thedivazo;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import thedivazo.config.ConfigManager;
import thedivazo.metrics.MetricsManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(name = PluginSettings.NAME_PLUGIN, version = PluginSettings.VERSION)
//@Dependency(value = "ProtocolLib")
//@SoftDependsOn(value = {
//        @SoftDependency(value = "PlaceholderAPI"),
//        @SoftDependency(value = "SuperVanish"),
//        @SoftDependency(value = "PremiumVanish"),
//        @SoftDependency(value = "ChatControllerRed"),
//        @SoftDependency(value = "Essentials"),
//        @SoftDependency(value = "CMI")
//})
@Author(value = "TheDiVaZo")
@ApiVersion(value = ApiVersion.Target.v1_13)
public class NamePlugin extends JavaPlugin {

    private PaperCommandManager manager = new PaperCommandManager(this);
    private static ConfigManager configManager;

    public static ConfigManager getConfigManager() {
        return NamePlugin.configManager;
    }

    public static NamePlugin getInstance() {
        return JavaPlugin.getPlugin(NamePlugin.class);
    }

    private static void setConfigManager(ConfigManager configManager) {
        NamePlugin.configManager = configManager;
    }

    @Override
    public void onEnable() {
        api.logging.Logger.init(new JULHandler(getLogger()));
        api.logging.Logger.info("Starting...");
        setConfigManager(new ConfigManager(NamePlugin.getInstance()));
        checkPluginVersion();
        new MetricsManager(this);
        registerEvents();
        registerCommands();
    }

    public void registerEvents() {

    }
    public void registerCommands() {
        manager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t)-> {
            getLogger().warning("Error occurred while executing command "+command.getName());
            return true;
        });
    }

    public void reloadConfigManager() {
        saveDefaultConfig();
        reloadConfig();
        getConfigManager().reloadConfigFile();
        registerEvents();
        registerCommands();
    }

    private void checkPluginVersion() {
        if (!PluginSettings.VERSION.equals(ConfigManager.getLastVersionOfPlugin())) {
            for (int i = 0; i < 5; i++) {
                Logger.warn("PLEASE, UPDATE NAME_PLUGIN! LINK: https://www.spigotmc.org/resources/link/");
            }
        } else {
            Logger.info("Plugin have last version");
        }
    }

    public static Float getVersion() {
        String version = Bukkit.getVersion();
        Pattern pattern = Pattern.compile("\\(MC: ([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(version);
        if (matcher.find())
        {
            return Float.parseFloat(matcher.group(1));
        }
        else return null;
    }
}