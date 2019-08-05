package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataObject
extends ConfigurationData
{
    @Override
    public Object get(FileConfiguration configuration, Class type, String path)
    {
        return configuration.get(path);
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isSet(path);
    }
}