/*
 *  This file is part of XConfiguration,
 *  licensed under the GNU General Public License v3.0.
 *
 *  Copyright (c) Xezard (Zotov Ivan)
 *
 *  XConfiguration is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XConfiguration is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XConfiguration. If not, see <https://www.gnu.org/licenses/>.
 */
package ru.xezard.configurations.bukkit.file;

import lombok.Getter;
import ru.xezard.configurations.bukkit.MemoryConfiguration;
import ru.xezard.configurations.bukkit.MemoryConfigurationOptions;

@Getter
public class FileConfigurationOptions
extends MemoryConfigurationOptions
{
    private String header;

    private boolean copyHeader = true;

    protected FileConfigurationOptions(MemoryConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    public FileConfiguration getConfiguration()
    {
        return (FileConfiguration) super.getConfiguration();
    }

    @Override
    public FileConfigurationOptions copyDefaults(boolean value)
    {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public FileConfigurationOptions pathSeparator(char value)
    {
        super.pathSeparator(value);
        return this;
    }

    public FileConfigurationOptions header(String value)
    {
        this.header = value;
        return this;
    }

    public FileConfigurationOptions copyHeader(boolean value)
    {
        this.copyHeader = value;
        return this;
    }
}