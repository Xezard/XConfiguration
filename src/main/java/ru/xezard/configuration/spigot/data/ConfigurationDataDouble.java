package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.util.NumberConversions.toDouble;

public class ConfigurationDataDouble
extends ConfigurationDataNumeric<Double>
{
    @Override
    public Double get(FileConfiguration configuration, Class<Double> type, String path)
    {
        return configuration.getDouble(path);
    }

    @Override
    public Double get(FileConfiguration configuration, Class<Double> type, String path, Double def)
    {
        Object value = configuration.get(path);

        return value instanceof Number ? toDouble(value) : def;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isDouble(path);
    }

    @Override
    public Double getDefault()
            {
                return 0d;
            }
}