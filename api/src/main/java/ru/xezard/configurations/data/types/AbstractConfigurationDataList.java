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
package ru.xezard.configurations.data.types;

import lombok.EqualsAndHashCode;
import ru.xezard.configurations.bukkit.file.FileConfiguration;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
public abstract class AbstractConfigurationDataList<T>
extends AbstractConfigurationData<List<T>>
{
    public AbstractConfigurationDataList(Class<?>... typeClasses)
    {
        super(ConfigurationDataType.LIST, typeClasses);
    }

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