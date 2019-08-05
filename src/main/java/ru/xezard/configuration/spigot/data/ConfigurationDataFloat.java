package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.util.NumberConversions.toDouble;

public class ConfigurationDataFloat
extends ConfigurationDataNumeric<Float>
{
    @Override
    public Float get(FileConfiguration configuration, Class<Float> type, String path)
    {
        return (float) configuration.getDouble(path);
    }

    @Override
    public Float get(FileConfiguration configuration, Class<Float> type, String path, Float def)
    {
        Object value = configuration.get(path);

        return value instanceof Number ? (float) toDouble(value) : def;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isDouble(path);
    }

    @Override
    public Float getDefault()
            {
                return 0f;
            }
}