package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListInt
extends AbstractConfigurationDataList<Integer>
{
    @Override
    public List<Integer> get(FileConfiguration configuration, Class<List<Integer>> type, String path)
    {
        return configuration.getIntegerList(path);
    }

    @Override
    public List<Integer> get(FileConfiguration configuration, Class<List<Integer>> type, String path, List<Integer> def)
    {
        return configuration.getList(path) == null ? def : configuration.getIntegerList(path);
    }
}