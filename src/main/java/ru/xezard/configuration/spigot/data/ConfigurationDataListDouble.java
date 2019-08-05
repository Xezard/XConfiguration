package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListDouble
extends AbstractConfigurationDataList<Double>
{
    @Override
    public List<Double> get(FileConfiguration configuration, Class<List<Double>> type, String path)
    {
        return configuration.getDoubleList(path);
    }

    @Override
    public List<Double> get(FileConfiguration configuration, Class<List<Double>> type, String path, List<Double> def)
    {
        return configuration.getList(path) == null ? def : configuration.getDoubleList(path);
    }
}