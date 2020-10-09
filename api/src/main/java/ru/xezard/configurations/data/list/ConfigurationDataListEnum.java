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
package ru.xezard.configurations.data.list;

import lombok.EqualsAndHashCode;
import ru.xezard.configurations.bukkit.file.FileConfiguration;
import ru.xezard.configurations.data.types.AbstractConfigurationDataList;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataListEnum<E extends Enum<E>>
extends AbstractConfigurationDataList<E>
{
    public ConfigurationDataListEnum()
    {
        super(Enum.class);
    }

    @Override
    public void set(FileConfiguration configuration, String path, List<E> values, Field field)
    {
        configuration.set(path, values.stream()
                                      .map(Enum::name)
                                      .collect(Collectors.toList()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> get(FileConfiguration configuration, String path, Field field)
    {
        if (!configuration.isSet(path))
        {
            return null;
        }

        List<E> enums = new ArrayList<> ();
        List<String> list = configuration.getStringList(path);

        for (String element : list)
        {
            try {
                enums.add(Enum.valueOf(((Class<E>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]), element));
            } catch (IllegalArgumentException e) {
                logger.warning("Can't get enum from string list: " + path);
                e.printStackTrace();
            }
        }

        return enums;
    }
}