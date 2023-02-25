package thedivazo.config;

import api.logging.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import thedivazo.DiceSystem;
import thedivazo.utils.ConfigUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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


    protected Map<String, String> conditions = new HashMap<>();
    protected List<Restrictions> restrictionsList = new ArrayList<>();

    public Map<String, String> getConditions() {
        return Collections.unmodifiableMap(conditions);
    }
    public List<Restrictions> getRestrictionsList() {
        return Collections.unmodifiableList(restrictionsList);
    }


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

    public void updateCondition() {
        conditions.clear();
        ConfigurationSection configurationSection = fileConfig.getConfigurationSection("condition");
        assert configurationSection!=null;
        for (String conditionName : configurationSection.getKeys(false)) {
            String condition = configurationSection.getString(conditionName);
            conditions.put(conditionName, condition);
        }
    }

    interface Restrictions {
        String getCondition();
        String getCountPoint();
    }

    public void updateRestrictions() {
        restrictionsList.clear();
        ConfigurationSection configurationSection = fileConfig.getConfigurationSection("restrictions");
        assert configurationSection != null;
        for (String rest : configurationSection.getKeys(false)) {
            ConfigurationSection restSection = configurationSection.getConfigurationSection(rest);
            assert restSection != null;
            restrictionsList.add(new Restrictions() {

                private final String condition = restSection.getString("condition");
                private final String countPoint = restSection.getString("count_point");
                @Override
                public String getCondition() {
                    return condition;
                }

                @Override
                public String getCountPoint() {
                    return countPoint;
                }
            });
        }
    }

    public String getConfigString() {
        return fileConfig.saveToString();
    }
}
