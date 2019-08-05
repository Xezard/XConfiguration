package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataString
extends ConfigurationData<String>
{
    @Override
    public String get(FileConfiguration configuration, Class<String> type, String path)
    {
        return configuration.getString(path);
    }

    @Override
    public String get(FileConfiguration configuration, Class<String> type, String path, String def)
    {
        return configuration.getString(path, def);
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isString(path);
    }

    @Override
    public String getDefault()
            {
                return "";
            }
}