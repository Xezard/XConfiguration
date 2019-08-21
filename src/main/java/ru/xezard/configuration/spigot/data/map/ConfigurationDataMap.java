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

package ru.xezard.configuration.spigot.data.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ru.xezard.configuration.spigot.data.ConfigurationManager;
import ru.xezard.configuration.spigot.data.types.AbstractConfigurationData;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataMap
extends AbstractConfigurationData<Map<String, ?>>
{
    public ConfigurationDataMap()
    {
        super(Map.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(FileConfiguration configuration, String path, Map<String, ?> values, Class<?>... types)
    {
        values.forEach((key, value) ->
        {
            Optional<AbstractConfigurationData> optionalConfigurationData = ConfigurationManager.getType(value.getClass());

            if (optionalConfigurationData.isPresent())
            {
                optionalConfigurationData.get().set(configuration, path + "." + key, value);
            } else {
                super.set(configuration, path, values);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, ?> get(FileConfiguration configuration, String path, Class<?>... types)
    {
        Map<String, Object> values = new HashMap<> ();

        ConfigurationSection section = configuration.getConfigurationSection(path);

        if (section != null)
        {
            for (Map.Entry<String, ?> entry : section.getValues(false).entrySet())
            {
                String key = entry.getKey();

                Class<?> objectType = entry.getValue().getClass();

                ConfigurationManager.getType(objectType).ifPresent((configurationData) ->
                {
                    values.put(key, configurationData.get(configuration, path + "." + key, objectType));
                });
            }
        }

        return values;
    }

    @Override
    public Map<String, ?> get(FileConfiguration configuration, String path, Map<String, ?> def, Class<?>... types)
    {
        Map<String, ?> map = this.get(configuration, path, types);

        return map.isEmpty() ? def : map;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isConfigurationSection(path);
    }

    @Override
    public Map<String, ?> getDefault()
    {
        return Collections.emptyMap();
    }
}