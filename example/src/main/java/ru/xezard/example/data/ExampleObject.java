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
package ru.xezard.example.data;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerializable;
import ru.xezard.configurations.bukkit.serialization.SerializableAs;

@Builder
@ToString
@AllArgsConstructor
@SerializableAs("ExampleObject")
public class ExampleObject
implements ConfigurationSerializable
{
    private String name;

    private int id;

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> serlialized = new HashMap<> ();

        serlialized.put("Name", this.name);
        serlialized.put("Id", this.id);

        return serlialized;
    }

    public static ExampleObject deserialize(Map<String, Object> serialized)
    {
        return ExampleObject.builder()
                            .name((String) serialized.get("Name"))
                            .id((int) serialized.get("Id"))
                            .build();
    }
}