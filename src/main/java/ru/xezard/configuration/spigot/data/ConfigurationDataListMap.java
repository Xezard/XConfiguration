package ru.xezard.configuration.spigot.data;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListMap
extends AbstractConfigurationDataList<Map<?, ?>>
{
    @Override
    public List<Map<?, ?>> get(FileConfiguration configuration, Class<List<Map<?, ?>>> type, String path)
    {
        return configuration.getMapList(path);
    }

    @Override
    public List<Map<?, ?>> get(FileConfiguration configuration, Class<List<Map<?, ?>>> type, String path, List<Map<?, ?>> def)
    {
        return configuration.getList(path) == null ? def : configuration.getMapList(path);
    }
}