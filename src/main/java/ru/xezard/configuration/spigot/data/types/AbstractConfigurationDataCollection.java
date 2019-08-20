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

package ru.xezard.configuration.spigot.data.types;

import java.util.Collection;
import java.util.Collections;

import lombok.EqualsAndHashCode;
import org.bukkit.configuration.file.FileConfiguration;

@EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
public abstract class AbstractConfigurationDataCollection<T>
extends AbstractConfigurationData<Collection<T>>
{
    public AbstractConfigurationDataCollection(Class<?>... typeClasses)
    {
        super(true, typeClasses);
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isList(path);
    }

    @Override
    public Collection<T> getDefault()
    {
        return Collections.emptyList();
    }
}