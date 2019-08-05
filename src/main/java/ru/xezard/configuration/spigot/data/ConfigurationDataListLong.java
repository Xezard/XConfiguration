package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListLong
extends AbstractConfigurationDataList<Long>
{
    @Override
    public List<Long> get(FileConfiguration configuration, Class<List<Long>> type, String path)
    {
        return configuration.getLongList(path);
    }

    @Override
    public List<Long> get(FileConfiguration configuration, Class<List<Long>> type, String path, List<Long> def)
    {
        return configuration.getList(path) == null ? def : configuration.getLongList(path);
    }
}