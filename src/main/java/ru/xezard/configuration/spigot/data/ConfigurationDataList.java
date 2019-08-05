package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataList
extends AbstractConfigurationDataList<Object>
{
    @Override
    @SuppressWarnings("unchecked")
    public List<Object> get(FileConfiguration configuration, Class<List<Object>> type, String path)
    {
        return (List<Object>) configuration.getList(path);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> get(FileConfiguration configuration, Class<List<Object>> type, String path, List<Object> def)
    {
        return configuration.getList(path) == null ? def : (List<Object>) configuration.getList(path);
    }
}