package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.util.NumberConversions.toInt;

public class ConfigurationDataShort
extends ConfigurationDataNumeric<Short>
{
    @Override
    public Short get(FileConfiguration configuration, Class<Short> type, String path)
    {
        return (short) configuration.getInt(path);
    }

    @Override
    public Short get(FileConfiguration configuration, Class<Short> type, String path, Short def)
    {
        Object value = configuration.get(path);

        return value instanceof Number ? (short) toInt(value) : def;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
            {
                return configuration.isInt(path);
            }

    @Override
    public Short getDefault()
            {
                return 0;
            }
}