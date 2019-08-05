package ru.xezard.configuration.spigot.data;

import java.util.Collections;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ru.xezard.configuration.spigot.ConfigurationType;

public class ConfigurationDataMap
extends ConfigurationData<Map<String, Object>>
{
    @Override
    public void set(FileConfiguration configuration, String path, Map<String, Object> values)
    {
        values.forEach((key, value) ->
        {
            ConfigurationType.getType(value.getClass(), false).ifPresentOrElse((type) ->
            {
                type.getDataType().set(configuration, path + "." + key, value);
            }, () -> configuration.set(path, values));
        });
    }
            
    @Override
    public Map<String, Object> get(FileConfiguration configuration, Class<Map<String, Object>> type, String path)
    {
        ConfigurationSection section = configuration.getConfigurationSection(path);

        return section != null ? section.getValues(false) : Collections.emptyMap();
    }

    @Override
    public Map<String, Object> get(FileConfiguration configuration, Class<Map<String, Object>> type, String path, Map<String, Object> def)
    {
        ConfigurationSection section = configuration.getConfigurationSection(path);

        return section == null ? def : section.getValues(false);
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isConfigurationSection(path);
    }

    @Override
    public Map<String, Object> getDefault()
    {
        return Collections.emptyMap();
    }
}