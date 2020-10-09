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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.xezard.configurations.Configuration;
import ru.xezard.configurations.bukkit.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.logging.Logger;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
public abstract class AbstractConfigurationData<T>
{
    protected static Logger logger = Logger.getLogger(Configuration.class.getName());

    private ConfigurationDataType type = ConfigurationDataType.OBJECT;

    private Class<?>[] typeClasses;

    public AbstractConfigurationData(Class<?>... typeClasses)
    {
        this.typeClasses = typeClasses;
    }

    public void set(FileConfiguration configuration, String path, T value, Field field)
    {
        configuration.set(path, value);
    }

    @SuppressWarnings("unchecked")
    public T get(FileConfiguration configuration, String path, Field field)
    {
        return (T) configuration.get(path);
    }

    public abstract boolean isValid(FileConfiguration configuration, String path);

    public T getDefault()
    {
        return null;
    }
}