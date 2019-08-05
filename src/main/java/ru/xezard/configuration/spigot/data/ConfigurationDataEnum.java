package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataEnum<E extends Enum<E>>
extends ConfigurationData<E>
{
    @Override
    public void set(FileConfiguration configuration, String path, E value)
    {
        configuration.set(path, value.name());
    }

    @Override
    public E get(FileConfiguration configuration, Class<E> type, String path)
    {
        String name = configuration.getString(path);

        if (name == null)
        {
            return null;
        }

        try {
            return Enum.valueOf(type, name);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public E get(FileConfiguration configuration, Class<E> type, String path, E def)
    {
        E value = this.get(configuration, type, path);

        return value == null ? def : value;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isString(path);
    }
}