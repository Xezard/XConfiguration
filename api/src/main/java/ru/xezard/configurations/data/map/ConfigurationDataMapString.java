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
package ru.xezard.configurations.data.map;

import lombok.EqualsAndHashCode;
import ru.xezard.configurations.bukkit.ConfigurationSection;
import ru.xezard.configurations.bukkit.file.FileConfiguration;
import ru.xezard.configurations.data.types.AbstractConfigurationData;
import ru.xezard.configurations.data.types.AbstractConfigurationDataMap;
import ru.xezard.configurations.manager.ConfigurationsDataManager;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataMapString
extends AbstractConfigurationDataMap<String>
{
    public ConfigurationDataMapString()
    {
        super(String.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(FileConfiguration configuration, String path, Map<String, ?> values, Field field)
    {
        values.forEach((key, value) ->
        {
            Optional<AbstractConfigurationData> optionalConfigurationData = ConfigurationsDataManager.getInstance().getType(value.getClass());

            if (optionalConfigurationData.isPresent())
            {
                optionalConfigurationData.get().set(configuration, path + "." + key, value, field);
            } else {
                logger.warning("[EAPI] Can't set value with key '" + key + "' to map: " + path);
            }
        });
    }

    @Override
    public Map<String, ?> get(FileConfiguration configuration, String path, Field field)
    {
        Map<String, Object> values = new HashMap<> ();

        ConfigurationSection section = configuration.getConfigurationSection(path);

        if (section == null)
        {
            return null;
        }

        section.getValues(false).forEach((key, value) ->
        {
            Class<?> objectType = value.getClass();

            Optional<AbstractConfigurationData> optionalConfigurationData = ConfigurationsDataManager.getInstance().getType(objectType);

            if (optionalConfigurationData.isPresent())
            {
                values.put(key, optionalConfigurationData.get().get(configuration, path + "." + key, field));
            } else {
                values.put(key, configuration.get(path + "." + key));
            }
        });

        return values.isEmpty() ? null : values;
    }

    @Override
    public Map<String, ?> getDefault()
    {
        return Collections.emptyMap();
    }
}