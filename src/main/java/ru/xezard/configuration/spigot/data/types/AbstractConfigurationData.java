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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
public abstract class AbstractConfigurationData<T>
{
    private boolean collection;

    private Class<?>[] typeClasses;

    public AbstractConfigurationData(Class<?>... typeClasses)
    {
        this.typeClasses = typeClasses;
    }

    public AbstractConfigurationData(boolean collection)
    {
        this.collection = collection;
    }

    public void set(FileConfiguration configuration, String path, T value, Class<?>... types)
    {
        configuration.set(path, value);
    }

    public boolean isSet(FileConfiguration configuration, String path)
    {
        return configuration.isSet(path);
    }

    @SuppressWarnings("unchecked")
    public T get(FileConfiguration configuration, String path, Class<?>... types)
    {
        return (T) configuration.get(path);
    }

    public T get(FileConfiguration configuration, String path, T def, Class<?>... types)
    {
        T value = this.get(configuration, path, types);

        return value == null ? def : value;
    }

    public abstract boolean isValid(FileConfiguration configuration, String path);

    public T getDefault()
    {
        return null;
    }
}