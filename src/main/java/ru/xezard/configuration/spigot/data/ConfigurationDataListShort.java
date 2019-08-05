package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListShort
extends AbstractConfigurationDataList<Short>
{
    @Override
    public List<Short> get(FileConfiguration configuration, Class<List<Short>> type, String path)
    {
        return configuration.getShortList(path);
    }

    @Override
    public List<Short> get(FileConfiguration configuration, Class<List<Short>> type, String path, List<Short> def)
    {
        return configuration.getList(path) == null ? def : configuration.getShortList(path);
    }
}