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

import com.google.common.base.Preconditions;
import lombok.Getter;

@Getter
public class YamlConfigurationOptions
extends FileConfigurationOptions
{
    private int indent = 2;

    protected YamlConfigurationOptions(YamlConfiguration configuration)
    {
        super(configuration);
    }

    @Override
    public YamlConfiguration getConfiguration()
    {
        return (YamlConfiguration) super.getConfiguration();
    }

    @Override
    public YamlConfigurationOptions copyDefaults(boolean value)
    {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions pathSeparator(char value)
    {
        super.pathSeparator(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions header(String value)
    {
        super.header(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions copyHeader(boolean value)
    {
        super.copyHeader(value);
        return this;
    }

    public YamlConfigurationOptions indent(int value)
    {
        Preconditions.checkArgument(value >= 2, "Indent must be at least 2 characters");
        Preconditions.checkArgument(value <= 9, "Indent cannot be greater than 9 characters");

        this.indent = value;
        return this;
    }
}