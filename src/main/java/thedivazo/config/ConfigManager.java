package thedivazo.config;

import api.logging.Logger;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import thedivazo.DiceSystem;
import thedivazo.utils.ConfigUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.constant.Constable;
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


    protected Map<String, String> variables = new HashMap<>();
    protected Map<String, String> conditions = new HashMap<>();
    protected Map<String, DiceType> diceTypes = new HashMap<>();


    public Map<String, String> getVariables() { return Collections.unmodifiableMap(variables); }
    public Map<String, String> getConditions() {
        return Collections.unmodifiableMap(conditions);
    }
    public Map<String, DiceType> getDiceTypes() {
        return Collections.unmodifiableMap(diceTypes);
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
        updateVariables();
        updateDiceTypes();
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

    public Constable placeholderSet(Player player, String placeholder) {
        if(isPAPILoaded()) {
            String condition = PlaceholderAPI.setPlaceholders(player, placeholder);
            if(NumberUtils.isCreatable(condition.toLowerCase(Locale.ROOT))) return NumberUtils.createDouble(condition);
            else if(!Objects.isNull(BooleanUtils.toBooleanObject(condition))) return BooleanUtils.toBooleanObject(condition);
            else return condition;
        }
        else throw new UnsupportedOperationException("PlaceholderAPI have not installed");
    }

    @Getter
    public static final class DiceType {
        private final String name;
        private final String diceCode;
        private final String text;
        private final String permission;

        public DiceType(String name, String diceCode, String text, String permission) {
            this.name = name;
            this.diceCode = diceCode;
            this.text = text;
            this.permission = permission;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (DiceType) obj;
            return Objects.equals(this.name, that.name) &&
                    Objects.equals(this.diceCode, that.diceCode) &&
                    Objects.equals(this.text, that.text) &&
                    Objects.equals(this.permission, that.permission);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, diceCode, text, permission);
        }

        @Override
        public String toString() {
            return "DiceType[" +
                    "name=" + name + ", " +
                    "diceCode=" + diceCode + ", " +
                    "text=" + text + ", " +
                    "permission=" + permission + ']';
        }


        }

    public void updateVariables() {
        ConfigurationSection variablesSection = fileConfig.getConfigurationSection("variables");
        if(Objects.isNull(variablesSection)) return;
        for (String key : variablesSection.getKeys(true)) {
            if(variablesSection.isString(key)) variables.put(key, variablesSection.getString(key));
        }
    }
    public void updateDiceTypes() {
        diceTypes.clear();
        ConfigurationSection configurationSection = fileConfig.getConfigurationSection("dice_types");
        assert configurationSection != null;
        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection diceTypeSection = configurationSection.getConfigurationSection(key);
            assert diceTypeSection != null;
            diceTypes.put(key,new DiceType(key, diceTypeSection.getString("dice"), diceTypeSection.getString("text"), diceTypeSection.getString("permission")));
        }
    }

    public String getConfigString() {
        return fileConfig.saveToString();
    }
}
