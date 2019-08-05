package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.util.NumberConversions.toLong;

public class ConfigurationDataLong
extends ConfigurationDataNumeric<Long>
{
    @Override
    public Long get(FileConfiguration configuration, Class<Long> type, String path)
    {
        return configuration.getLong(path);
    }

    @Override
    public Long get(FileConfiguration configuration, Class<Long> type, String path, Long def)
    {
        Object value = configuration.get(path, def);

        return value instanceof Number ? toLong(value) : def;
    }

    @Override
    public boolean isValid(final FileConfiguration configuration, final String path)
    {
        return configuration.isLong(path);
    }

    @Override
    public Long getDefault()
            {
                return 0L;
            }
}