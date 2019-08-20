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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.EqualsAndHashCode;
import org.bukkit.configuration.file.FileConfiguration;
import ru.xezard.configuration.spigot.data.types.AbstractConfigurationDataCollection;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataCollectionString
extends AbstractConfigurationDataCollection<String>
{
    public ConfigurationDataCollectionString()
    {
        super(String.class);
    }

    @Override
    public Collection<String> get(FileConfiguration configuration, String path, Class<?>... type)
    {
        Map<Map<List<String>, Integer>, TreeMap<Integer, String>> map;
        return configuration.getStringList(path);
    }

    @Override
    public Collection<String> get(FileConfiguration configuration, String path, Collection<String> def, Class<?>... type)
    {
        return this.isValid(configuration, path) ? configuration.getStringList(path) : def;
    }
}