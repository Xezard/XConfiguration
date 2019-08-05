package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListBoolean
extends AbstractConfigurationDataList<Boolean>
{
    @Override
    public List<Boolean> get(FileConfiguration configuration, Class<List<Boolean>> type, String path)
    {
        return configuration.getBooleanList(path);
    }

    @Override
    public List<Boolean> get(FileConfiguration configuration, Class<List<Boolean>> type, String path, List<Boolean> def)
    {
        return configuration.getList(path) == null ? def : configuration.getBooleanList(path);
    }
}