package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.util.NumberConversions.toInt;

public class ConfigurationDataInt
extends ConfigurationDataNumeric<Integer>
{
    @Override
    public Integer get(FileConfiguration configuration, Class<Integer> type, String path)
    {
        return configuration.getInt(path);
    }

    @Override
    public Integer get(FileConfiguration configuration, Class<Integer> type, String path, Integer def)
    {
        Object value = configuration.get(path);

        if (value == null)
        {
            return def;
        }

        return value instanceof Number ? toInt(value) : def;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isInt(path);
    }

    @Override
    public Integer getDefault()
            {
                return 0;
            }
}