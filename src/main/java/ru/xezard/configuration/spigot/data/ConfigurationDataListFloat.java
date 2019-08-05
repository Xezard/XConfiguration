package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListFloat
extends AbstractConfigurationDataList<Float>
{
    @Override
    public List<Float> get(FileConfiguration configuration, Class<List<Float>> type, String path)
    {
        return configuration.getFloatList(path);
    }

    @Override
    public List<Float> get(FileConfiguration configuration, Class<List<Float>> type, String path, List<Float> def)
    {
        return configuration.getList(path) == null ? def : configuration.getFloatList(path);
    }
}