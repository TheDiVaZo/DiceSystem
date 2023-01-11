package thedivazo.config;

import api.logging.Logger;
import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import thedivazo.DiceSystem;
import thedivazo.utils.ConfigUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.bukkit.Bukkit.getServer;

public class ConfigManager {

    @Getter
    private final DiceSystem plugin;
    private FileConfiguration fileConfig;
    @Getter
    private ConfigUtils configUtils;

    @Getter
    private boolean isPAPILoaded = false;
    @Getter
    private boolean isVault = false;


    @Getter
    private Permission permissionVault = null;

    public ConfigManager(DiceSystem plugin) {
        this.plugin = plugin;
        this.fileConfig = plugin.getConfig();
        this.configUtils = new ConfigUtils(fileConfig);
        plugin.saveDefaultConfig();
        updateConfig();
        reloadConfigFile();
    }

    private boolean isPlugin(String pluginName) {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null;
    }


    private void saveSoftDependCondition() {
        if(isPlugin("Vault")) {
            this.isVault = true;
            RegisteredServiceProvider<Permission> rsp1 = getServer().getServicesManager().getRegistration(Permission.class);
            if(rsp1!= null) {
                permissionVault = rsp1.getProvider();
            }
        }
        if(isPlugin("PlaceholderAPI")) {
            this.isPAPILoaded = true;
        }
    }

    public void reloadConfigFile() {
        this.fileConfig = plugin.getConfig();
        this.configUtils.setConfig(fileConfig);
        saveSoftDependCondition();
    }



    public static String getLastVersionOfPlugin() {
        String inputLine;
        Logger.info("Check updates...");
        try {
            URL obj = new URL("https://api.spigotmc.org/legacy/update.php?resource=number");
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            Logger.warn("ERROR GETTING LAST VERSION!");
            return "error";
        }
    }

    public void updateConfig() {
        //todo: write method
    }

    public String getConfigString() {
        return fileConfig.saveToString();
    }
}
