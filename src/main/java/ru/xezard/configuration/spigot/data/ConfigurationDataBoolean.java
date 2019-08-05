package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataBoolean
extends ConfigurationData<Boolean>
{
    @Override
    public Boolean get(FileConfiguration configuration, Class<Boolean> type, String path)
    {
        return configuration.getBoolean(path);
    }

    @Override
    public Boolean get(FileConfiguration configuration, Class<Boolean> type, String path, Boolean def)
    {
        Object value = configuration.get(path, def);

        return value instanceof Boolean ? (Boolean) value : def;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isBoolean(path);
    }
}