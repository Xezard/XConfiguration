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
package ru.xezard.configurations.bukkit.file;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;
import ru.xezard.configurations.bukkit.ConfigurationSection;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerializable;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerialization;

import java.util.LinkedHashMap;
import java.util.Map;

public class YamlRepresenter
extends Representer
{
    public YamlRepresenter()
    {
        this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
        this.multiRepresenters.put(ConfigurationSerializable.class, new RepresentConfigurationSerializable());
    }

    private class RepresentConfigurationSection
    extends RepresentMap
    {
        @Override
        public Node representData(Object data)
        {
            return super.representData(((ConfigurationSection) data).getValues(false));
        }
    }

    private class RepresentConfigurationSerializable
    extends RepresentMap
    {
        @Override
        public Node representData(Object data)
        {
            Map<String, Object> values = new LinkedHashMap<> ();

            ConfigurationSerializable serializable = (ConfigurationSerializable) data;

            values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
            values.putAll(serializable.serialize());

            return super.representData(values);
        }
    }
}