package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListChar
extends AbstractConfigurationDataList<Character>
{
    @Override
    public List<Character> get(FileConfiguration configuration, Class<List<Character>> type, String path)
    {
        return configuration.getCharacterList(path);
    }

    @Override
    public List<Character> get(FileConfiguration configuration, Class<List<Character>> type, String path, List<Character> def)
    {
        return configuration.getList(path) == null ? def : configuration.getCharacterList(path);
    }
}