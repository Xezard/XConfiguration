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
package ru.xezard.configurations.manager;

import com.google.common.collect.Lists;
import ru.xezard.configurations.data.base.*;
import ru.xezard.configurations.data.list.*;
import ru.xezard.configurations.data.map.ConfigurationDataMapEnum;
import ru.xezard.configurations.data.map.ConfigurationDataMapInteger;
import ru.xezard.configurations.data.map.ConfigurationDataMapString;
import ru.xezard.configurations.data.types.AbstractConfigurationData;
import ru.xezard.configurations.data.types.ConfigurationDataType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ConfigurationsDataManager
implements IConfigurationsDataManager
{
    private static final ConfigurationDataList LIST = new ConfigurationDataList();
    private static final ConfigurationDataMapString MAP = new ConfigurationDataMapString();

    private final List<AbstractConfigurationData> configurationDataList = Lists.newArrayList
    (
            // base primitive objects

            new ConfigurationDataBoolean(),
            new ConfigurationDataByte(),
            new ConfigurationDataCharacter(),
            new ConfigurationDataDouble(),
            new ConfigurationDataEnum<>(),
            new ConfigurationDataFloat(),
            new ConfigurationDataInteger(),
            new ConfigurationDataLong(),
            new ConfigurationDataObject(),
            new ConfigurationDataShort(),
            new ConfigurationDataString(),

            // objects in list

            new ConfigurationDataListBoolean(),
            new ConfigurationDataListByte(),
            new ConfigurationDataListByte(),
            new ConfigurationDataListCharacter(),
            new ConfigurationDataListDouble(),
            new ConfigurationDataListEnum<> (),
            new ConfigurationDataFloat(),
            new ConfigurationDataListFloat(),
            new ConfigurationDataListInteger(),
            new ConfigurationDataListLong(),
            new ConfigurationDataListMap(),
            new ConfigurationDataListShort(),
            new ConfigurationDataListString(),
            LIST,

            new ConfigurationDataMapEnum<> (),
            new ConfigurationDataMapInteger(),
            new ConfigurationDataMapString(),
            MAP
    );

    private static volatile ConfigurationsDataManager instance;

    public static ConfigurationsDataManager getInstance()
    {
        if (instance == null)
        {
            synchronized (ConfigurationsDataManager.class)
            {
                if (instance == null)
                {
                    instance = new ConfigurationsDataManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void register(AbstractConfigurationData<?> configurationData)
    {
        this.configurationDataList.add(configurationData);
    }

    @Override
    public void remove(AbstractConfigurationData<?> configurationData)
    {
        this.configurationDataList.remove(configurationData);
    }

    @Override
    public Optional<AbstractConfigurationData> getType(Field field)
    {
        Class<?> fieldType = field.getType();

        Optional<AbstractConfigurationData> optionalConfigurationData = this.getType(ConfigurationDataType.OBJECT, fieldType);

        if (List.class.isAssignableFrom(fieldType))
        {
            Type fieldGenericType = field.getGenericType();

            if (fieldGenericType instanceof ParameterizedType)
            {
                Type typeArgument = ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];

                optionalConfigurationData = this.getType(ConfigurationDataType.LIST, (Class<?>) typeArgument);

                return optionalConfigurationData.isPresent() ? optionalConfigurationData : Optional.of(LIST);
            }
        }

        if (Map.class.isAssignableFrom(fieldType))
        {
            Type fieldGenericType = field.getGenericType();

            if (fieldGenericType instanceof ParameterizedType)
            {
                Type typeArgument = ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];

                optionalConfigurationData = this.getType(ConfigurationDataType.MAP, (Class<?>) typeArgument);

                return optionalConfigurationData.isPresent() ? optionalConfigurationData : Optional.of(MAP);
            }
        }

        return optionalConfigurationData;
    }

    @Override
    public Optional<AbstractConfigurationData> getType(Class<?> clazz)
    {
        return this.configurationDataList.stream()
                                         .filter((configurationData) ->
        {
            return Stream.of(configurationData.getTypeClasses())
                         .anyMatch((typeClass) -> typeClass.isAssignableFrom(clazz));
        }).findFirst();
    }

    protected Optional<AbstractConfigurationData> getType(ConfigurationDataType dataType, Class<?> clazz)
    {
        return this.configurationDataList.stream()
                                         .filter((configurationData) -> configurationData.getType() == dataType)
                                         .filter((configurationData) ->
        {
            return Stream.of(configurationData.getTypeClasses())
                         .anyMatch((typeClass) -> typeClass.isAssignableFrom(clazz));
        }).findFirst();
    }
}