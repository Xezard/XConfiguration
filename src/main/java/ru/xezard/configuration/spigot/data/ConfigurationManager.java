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

package ru.xezard.configuration.spigot.data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;
import ru.xezard.configuration.spigot.data.base.*;
import ru.xezard.configuration.spigot.data.collections.*;
import ru.xezard.configuration.spigot.data.map.ConfigurationDataMap;
import ru.xezard.configuration.spigot.data.special.*;
import ru.xezard.configuration.spigot.data.types.AbstractConfigurationData;

@UtilityClass
public class ConfigurationManager
{
    private final ConfigurationDataCollection LIST = new ConfigurationDataCollection();

    private final List<AbstractConfigurationData> CONFIGURATION_DATAS = new ArrayList<AbstractConfigurationData> ()
    {{
        this.add(new ConfigurationDataBoolean());
        this.add(new ConfigurationDataByte());
        this.add(new ConfigurationDataCharacter());
        this.add(new ConfigurationDataDouble());
        this.add(new ConfigurationDataEnum());
        this.add(new ConfigurationDataFloat());
        this.add(new ConfigurationDataInteger());
        this.add(new ConfigurationDataLong());
        this.add(new ConfigurationDataShort());
        this.add(new ConfigurationDataString());

        this.add(LIST);
        this.add(new ConfigurationDataCollectionBoolean());
        this.add(new ConfigurationDataCollectionByte());
        this.add(new ConfigurationDataCollectionCharacter());
        this.add(new ConfigurationDataCollectionDouble());
        this.add(new ConfigurationDataCollectionFloat());
        this.add(new ConfigurationDataCollectionInteger());
        this.add(new ConfigurationDataCollectionLong());
        this.add(new ConfigurationDataCollectionMap());
        this.add(new ConfigurationDataCollectionShort());
        this.add(new ConfigurationDataCollectionString());

        this.add(new ConfigurationDataMap());

        this.add(new ConfigurationDataColor());
        this.add(new ConfigurationDataItemStack());
        this.add(new ConfigurationDataOfflinePlayer());
        this.add(new ConfigurationDataVector());
    }};

    public void register(AbstractConfigurationData configurationData)
    {
        CONFIGURATION_DATAS.add(configurationData);
    }

    public void remove(AbstractConfigurationData configurationData)
    {
        CONFIGURATION_DATAS.remove(configurationData);
    }

    protected Optional<AbstractConfigurationData> getType(Field field)
    {
        Class<?> fieldType = field.getType();

        Optional<AbstractConfigurationData> optionalConfigurationData = getType(fieldType, false);

        if (List.class.isAssignableFrom(fieldType))
        {
            Type fieldGenericType = field.getGenericType();

            if (fieldGenericType instanceof ParameterizedType)
            {
                Type typeArgument = ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];

                optionalConfigurationData = getType((Class<?>) typeArgument, true);

                return optionalConfigurationData.isPresent() ? optionalConfigurationData : Optional.of(LIST);
            }
        }

        return optionalConfigurationData;
    }

    @SuppressWarnings("unchecked")
    public Optional<AbstractConfigurationData> getType(Class<?> clazz)
    {
        return CONFIGURATION_DATAS.stream().filter((configurationData) ->
        {
            return Stream.of(configurationData.getTypeClasses())
                         .anyMatch((typeClass) -> typeClass.isAssignableFrom(clazz));
        }).findAny();
    }

    @SuppressWarnings("unchecked")
    protected Optional<AbstractConfigurationData> getType(Class<?> clazz, boolean isCollection)
    {
        return CONFIGURATION_DATAS.stream()
                                  .filter((configurationData) -> configurationData.isCollection() == isCollection)
                                  .filter((configurationData) ->
        {
            return Stream.of(configurationData.getTypeClasses())
                         .anyMatch((typeClass) -> typeClass.isAssignableFrom(clazz));
        }).findAny();
    }
}