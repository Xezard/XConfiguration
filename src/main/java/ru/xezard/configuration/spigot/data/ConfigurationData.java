package ru.xezard.configuration.spigot.data;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class ConfigurationData<T>
{
    public void set(FileConfiguration configuration, String path, T value)
    {
        configuration.set(path, value);
    }

    public boolean isSet(FileConfiguration configuration, String path)
    {
        return configuration.isSet(path);
    }

    @SuppressWarnings("unchecked")
    public T get(FileConfiguration configuration, Class<T> type, String path)
    {
        return (T) configuration.get(path);
    }

    public T get(FileConfiguration configuration, Class<T> type, String path, T def)
    {
        T value = this.get(configuration, type, path);

        return value == null ? def : value;
    }

    public abstract boolean isValid(FileConfiguration configuration, String path);

    public T getDefault()
    {
        return null;
    }
}