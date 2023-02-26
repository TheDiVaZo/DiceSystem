package thedivazo.config;

import api.logging.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import thedivazo.DiceSystem;
import thedivazo.dice.DiceManager;
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
    protected List<Restriction> restrictionList = new ArrayList<>();
    protected List<DiceType> diceTypeList = new ArrayList<>();


    public Map<String, String> getVariables() { return Collections.unmodifiableMap(variables); }
    public Map<String, String> getConditions() {
        return Collections.unmodifiableMap(conditions);
    }
    public List<Restriction> getRestrictionList() {
        return Collections.unmodifiableList(restrictionList);
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
        updateConditions();
        updateRestrictions();
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

    @RequiredArgsConstructor
    @Getter
    record Restriction(String condition, String codeGetPoint) {
    }

    @RequiredArgsConstructor
    @Getter
    record DiceType(String name, String diceCode, String text, String permission) {

    }

    public void updateVariables() {
        variables.clear();
        ConfigurationSection configurationSection = fileConfig.getConfigurationSection("variables");
        assert configurationSection != null;
        for (String key : configurationSection.getKeys(false)) {
            variables.put(key, configurationSection.getString(key));
        }
    }
    public void updateConditions() {
        conditions.clear();
        ConfigurationSection configurationSection = fileConfig.getConfigurationSection("condition");
        assert configurationSection!=null;
        for (String conditionName : configurationSection.getKeys(false)) {
            String condition = configurationSection.getString(conditionName);
            conditions.put(conditionName, condition);
        }
    }
    public void updateRestrictions() {
        restrictionList.clear();
        ConfigurationSection configurationSection = fileConfig.getConfigurationSection("restrictions");
        assert configurationSection != null;
        for (String rest : configurationSection.getKeys(false)) {
            ConfigurationSection restSection = configurationSection.getConfigurationSection(rest);
            assert restSection != null;
            restrictionList.add(new Restriction(restSection.getString("condition"), restSection.getString("count_point")));
        }
    }
    public void updateDiceTypes() {
        diceTypeList.clear();
        ConfigurationSection configurationSection = fileConfig.getConfigurationSection("dice_types");
        assert configurationSection != null;
        for (String key : configurationSection.getKeys(false)) {
            ConfigurationSection diceTypeSection = configurationSection.getConfigurationSection(key);
            assert diceTypeSection != null;
            diceTypeList.add(new DiceType(key, diceTypeSection.getString("dice"), diceTypeSection.getString("text"), diceTypeSection.getString("permission")));
        }
    }

    public String getConfigString() {
        return fileConfig.saveToString();
    }
}
