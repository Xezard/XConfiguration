package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataChar
extends ConfigurationData<Character>
{
    @Override
    public Character get(FileConfiguration configuration, Class<Character> type, String path)
    {
        return configuration.getString(path).charAt(0);
    }

    @Override
    public Character get(FileConfiguration configuration, Class<Character> type, String path, Character def)
    {
        String value = configuration.getString(path);

        return value == null || value.isEmpty() ? def : value.charAt(0);
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
            {
                return configuration.isInt(path);
            }

    @Override
    public Character getDefault()
            {
                return 0;
            }
}