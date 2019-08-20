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
import org.bukkit.configuration.file.FileConfiguration;
import ru.xezard.configuration.spigot.data.types.AbstractConfigurationData;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataLong
extends AbstractConfigurationData<Long>
{
    public ConfigurationDataLong()
    {
        super(Long.class, long.class);
    }

    @Override
    public Long get(FileConfiguration configuration, String path, Class<?>... type)
    {
        return configuration.getLong(path);
    }

    @Override
    public Long get(FileConfiguration configuration, String path, Long def, Class<?>... type)
    {
        return configuration.getLong(path, def);
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isLong(path);
    }

    @Override
    public Long getDefault()
    {
        return Long.MIN_VALUE;
    }
}