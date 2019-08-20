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

import static org.bukkit.util.NumberConversions.toByte;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataByte
extends AbstractConfigurationData<Byte>
{
    public ConfigurationDataByte()
    {
        super(Byte.class, byte.class);
    }

    @Override
    public Byte get(FileConfiguration configuration, String path, Class<?>... type)
    {
        return toByte(configuration.getInt(path));
    }

    @Override
    public Byte get(FileConfiguration configuration, String path, Byte def, Class<?>... type)
    {
        Object value = configuration.get(path, def);

        return value instanceof Number ? toByte(value) : def;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isInt(path);
    }

    @Override
    public Byte getDefault()
    {
        return Byte.MIN_VALUE;
    }
}