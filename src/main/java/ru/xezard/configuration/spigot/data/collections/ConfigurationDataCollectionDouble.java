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

package ru.xezard.configuration.spigot.data.collections;

import java.util.Collection;

import lombok.EqualsAndHashCode;
import org.bukkit.configuration.file.FileConfiguration;
import ru.xezard.configuration.spigot.data.types.AbstractConfigurationDataCollection;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataCollectionDouble
extends AbstractConfigurationDataCollection<Double>
{
    public ConfigurationDataCollectionDouble()
    {
        super(Double.class, double.class);
    }

    @Override
    public Collection<Double> get(FileConfiguration configuration, String path, Class<?>... type)
    {
        return configuration.getDoubleList(path);
    }

    @Override
    public Collection<Double> get(FileConfiguration configuration, String path, Collection<Double> def, Class<?>... type)
    {
        return this.isValid(configuration, path) ? configuration.getDoubleList(path) : def;
    }
}