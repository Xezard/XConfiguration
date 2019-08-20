/*
 * Copyright 2019 Xezard [Zotov I.]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.xezard.configuration.spigot.data.base;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import ru.xezard.configuration.spigot.data.types.AbstractConfigurationData;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataEnum<E extends Enum<E>>
extends AbstractConfigurationData<E>
{
    public ConfigurationDataEnum()
    {
        super(Enum.class);
    }

    @Override
    public void set(FileConfiguration configuration, String path, E value, Class<?>... type)
    {
        configuration.set(path, value.name());
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public E get(FileConfiguration configuration, String path, Class<?>... type)
    {
        String name = configuration.getString(path);

        if (StringUtils.isBlank(name) || type.length < 1)
        {
            return null;
        }

        try {
            return (E) Enum.valueOf((Class<Enum>) type[0], name);
        } catch (final IllegalArgumentException e) {
            Bukkit.getLogger().warning("[EAPI] Cannot get enum from string: " + path);
            return null;
        }
    }

    @Override
    public E get(FileConfiguration configuration, String path, E def, Class<?>... type)
    {
        E value = this.get(configuration, path, type);

        return value == null ? def : value;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isString(path);
    }
}