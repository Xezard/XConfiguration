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
package ru.xezard.configurations.data.base;

import lombok.EqualsAndHashCode;
import ru.xezard.configurations.bukkit.file.FileConfiguration;
import ru.xezard.configurations.data.types.AbstractConfigurationData;

import java.lang.reflect.Field;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataString
extends AbstractConfigurationData<String>
{
    public ConfigurationDataString()
    {
        super(String.class);
    }

    @Override
    public String get(FileConfiguration configuration, String path, Field field)
    {
        return configuration.isSet(path) ? configuration.getString(path) : null;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isString(path);
    }

    @Override
    public String getDefault()
    {
        return "";
    }
}