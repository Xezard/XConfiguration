package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.util.NumberConversions.toInt;

public class ConfigurationDataByte
extends ConfigurationDataNumeric<Byte>
{
    @Override
    public Byte get(FileConfiguration configuration, Class<Byte> type, String path)
    {
        return (byte) configuration.getInt(path);
    }

    @Override
    public Byte get(FileConfiguration configuration, Class<Byte> type, String path, Byte def)
    {
        Object value = configuration.get(path);

        return value instanceof Number ? (byte) toInt(value) : def;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isInt(path);
    }

    @Override
    public Byte getDefault()
            {
                return 0;
            }
}