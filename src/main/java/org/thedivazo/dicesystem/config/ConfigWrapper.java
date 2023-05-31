package org.thedivazo.dicesystem.config;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@AllArgsConstructor
public class ConfigWrapper {
    private ConfigurationSection configurationSection;

    public ConfigWrapper getRequiredConfigurationSection(@NotNull String path) throws InvalidConfigurationException {
        if (!configurationSection.isConfigurationSection(path)) throw new InvalidConfigurationException("There is no '"+path+"' section in the config.");
        else return new ConfigWrapper(configurationSection.getConfigurationSection(path));
    }

    public String getString(String path, String def) {
        return configurationSection.getString(path, def);
    }

    public String getRequiredString(String path) throws InvalidConfigurationException {
        if (!configurationSection.isString(path)) throw new InvalidConfigurationException("There is no '"+path+"' string field in the config.");
        return configurationSection.getString(path);
    }

    public int getInt(String path, int def) {
        return configurationSection.getInt(path, def);
    }

    public int getRequiredInt(String path) throws InvalidConfigurationException {
        if (!configurationSection.isInt(path)) throw new InvalidConfigurationException("There is no '"+path+"' number field in the config.");
        return configurationSection.getInt(path);
    }

    public long getLong(String path, long def) {
        return configurationSection.getLong(path, def);
    }

    public long getRequiredLong(String path) throws InvalidConfigurationException {
        if (!configurationSection.isLong(path)) throw new InvalidConfigurationException("There is no '"+path+"' number field in the config.");
        return configurationSection.getLong(path);
    }

    public double getDouble(String path, double def) {
        return configurationSection.getDouble(path, def);
    }

    public double getRequiredDouble(String path) throws InvalidConfigurationException {
        if (!configurationSection.isDouble(path)) throw new InvalidConfigurationException("There is no '"+path+"' number field in the config.");
        return configurationSection.getDouble(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return configurationSection.getBoolean(path, def);
    }

    public boolean getRequiredBoolean(String path) throws InvalidConfigurationException {
        if (!configurationSection.isBoolean(path)) throw new InvalidConfigurationException("There is no '"+path+"' boolean field in the config.");
        return configurationSection.isBoolean(path);
    }

    public List<String> getStringList(String path, List<String> def) {
        if(!configurationSection.isList(path)) return def;
        return configurationSection.getStringList(path);
    }

    public List<String> getRequireStringList(String path) throws InvalidConfigurationException {
        if(!configurationSection.isList(path)) throw new InvalidConfigurationException("There is no '"+path+"' section for string list in the config.");;
        return configurationSection.getStringList(path);
    }

    public <E> List<E> getList(String path, Class<E> elementClazz, List<E> def) {
        if(!configurationSection.isList(path)) return def;
        return configurationSection.getList(path).stream().map(elementClazz::cast).toList();
    }

    public <E> List<E> getRequireList(String path, Class<E> elementClazz) throws InvalidConfigurationException {
        if(!configurationSection.isList(path)) throw new InvalidConfigurationException("There is no '"+path+"' list section in the config.");;
        return configurationSection.getList(path).stream().map(elementClazz::cast).toList();
    }

    public <V> Map<String,V> getMap(String path, Class<V> valueClazz, Map<String, V> def) {
        if(!configurationSection.contains(path)) return def;
        Map<String, V> result = new HashMap<>();
        for (String key : configurationSection.getKeys(false)) {
            result.put(key, configurationSection.getObject(key, valueClazz));
        }
        return result;
    }

    public <V> Map<String,V> getRequiredMap(String path, Class<V> valueClazz) throws InvalidConfigurationException {
        if(!configurationSection.contains(path)) throw new InvalidConfigurationException("There is no '"+path+"' section in the config.");
        Map<String, V> result = new HashMap<>();
        for (String key : configurationSection.getKeys(false)) {
            result.put(key, configurationSection.getObject(key, valueClazz));
        }
        return result;
    }

    public <T> T getObject(String path, Class<T> clazz, T def) {
        return configurationSection.getObject(path, clazz, def);
    }

    public <T> T getObject(String path, Class<T> clazz) throws InvalidConfigurationException {
        if (!configurationSection.contains(path)) throw new InvalidConfigurationException("There is no '"+path+"' field in the config.");
        return configurationSection.getObject(path, clazz);
    }

    public Set<String> getKeys(boolean deep) {
        return configurationSection.getKeys(deep);
    }


}
