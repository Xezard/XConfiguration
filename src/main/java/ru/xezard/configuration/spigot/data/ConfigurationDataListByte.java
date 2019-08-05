package ru.xezard.configuration.spigot.data;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationDataListByte
extends AbstractConfigurationDataList<Byte>
{
    @Override
    public List<Byte> get(FileConfiguration configuration, Class<List<Byte>> type, String path)
    {
        return configuration.getByteList(path);
    }

    @Override
    public List<Byte> get(FileConfiguration configuration, Class<List<Byte>> type, String path, List<Byte> def)
    {
        return configuration.getList(path) == null ? def : configuration.getByteList(path);
    }
}