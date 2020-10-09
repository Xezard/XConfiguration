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
public class ConfigurationDataEnum<E extends Enum<E>>
extends AbstractConfigurationData<E>
{
    public ConfigurationDataEnum()
    {
        super(Enum.class);
    }

    @Override
    public void set(FileConfiguration configuration, String path, E value, Field field)
    {
        configuration.set(path, value.name());
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(FileConfiguration configuration, String path, Field field)
    {
        String name = configuration.getString(path);

        if (name == null || name.isEmpty())
        {
            return null;
        }

        try {
            return Enum.valueOf((Class<E>) field.getType(), name);
        } catch (IllegalArgumentException e) {
            logger.warning("Can't retrieve enum from string: " + path);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isString(path);
    }
}