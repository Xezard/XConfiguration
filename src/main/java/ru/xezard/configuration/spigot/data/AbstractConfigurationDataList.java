package ru.xezard.configuration.spigot.data;

import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class AbstractConfigurationDataList<T>
extends ConfigurationData<List<T>>
{
    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isList(path);
    }

    @Override
    public List<T> getDefault()
    {
        return Collections.emptyList();
    }
}