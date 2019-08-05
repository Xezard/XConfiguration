package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListString
extends AbstractConfigurationDataList<String>
{
    @Override
    public List<String> get(FileConfiguration configuration, Class<List<String>> type, String path)
    {
        return configuration.getStringList(path);
    }

    @Override
    public List<String> get(FileConfiguration configuration, Class<List<String>> type, String path, List<String> def)
    {
        return configuration.getList(path) == null ? def : configuration.getStringList(path);
    }
}