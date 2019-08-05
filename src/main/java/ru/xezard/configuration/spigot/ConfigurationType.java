package ru.xezard.configuration.spigot;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;
import ru.xezard.configuration.spigot.data.*;

@Getter
public enum ConfigurationType
{
    BOOLEAN(new ConfigurationDataBoolean(), boolean.class, Boolean.class),
    BYTE(new ConfigurationDataByte(), byte.class, Byte.class),
    SHORT(new ConfigurationDataShort(), short.class, Short.class),
    INT(new ConfigurationDataInt(), int.class, Integer.class),
    LONG(new ConfigurationDataLong(), long.class, Long.class),
    FLOAT(new ConfigurationDataFloat(), float.class, Float.class),
    DOUBLE(new ConfigurationDataDouble(), double.class, Double.class),
    CHAR(new ConfigurationDataChar(), char.class, Character.class),
    STRING(new ConfigurationDataString(), String.class),
    ENUM(new ConfigurationDataEnum(), Enum.class),
    MAP(new ConfigurationDataMap(), Map.class),
    LIST(new ConfigurationDataList(), true),
    BOOLEAN_LIST(new ConfigurationDataListBoolean(), true, boolean.class, Boolean.class),
    BYTE_LIST(new ConfigurationDataListByte(), true, byte.class, Byte.class),
    SHORT_LIST(new ConfigurationDataListShort(), true, short.class, Short.class),
    INT_LIST(new ConfigurationDataListInt(), true, int.class, Integer.class),
    LONG_LIST(new ConfigurationDataListLong(), true, long.class, Long.class),
    FLOAT_LIST(new ConfigurationDataListFloat(), true, float.class, Float.class),
    DOUBLE_LIST(new ConfigurationDataListDouble(), true, double.class, Double.class),
    CHAR_LIST(new ConfigurationDataListChar(), true, char.class, Character.class),
    STRING_LIST(new ConfigurationDataListString(), true, String.class),
    MAP_LIST(new ConfigurationDataListMap(), true, Map.class),
    OBJECT(new ConfigurationDataObject());

    private Class<?>[] typeClasses;

    private ConfigurationData dataType;

    private boolean list;

    ConfigurationType(ConfigurationData dataType, Class... typeClasses)
    {
        this(dataType, false, typeClasses);
    }

    ConfigurationType(ConfigurationData dataType, boolean list, Class... typeClasses)
    {
        this.dataType = dataType;
        this.list = list;
        this.typeClasses = typeClasses;
    }

    public static Optional<ConfigurationType> getType(Field field)
    {
        Class<?> fieldType = field.getType();

        if (List.class.isAssignableFrom(fieldType))
        {
            Type fieldGenericType = field.getGenericType();

            if (fieldGenericType instanceof ParameterizedType)
            {
                Type typeArgument = ((ParameterizedType) fieldGenericType).getActualTypeArguments()[0];

                Optional<ConfigurationType> type = getType((Class<?>) typeArgument, true);

                return type.isEmpty() ? Optional.of(LIST) : type;
            }
        }

        return getType(field.getType(), false);
    }

    public static Optional<ConfigurationType> getType(Class<?> clazz, boolean isList)
    {
        return Stream.of(values())
                     .filter((type) -> type.isList() == isList)
                     .filter((type) -> Stream.of(type.getTypeClasses())
                                             .anyMatch((typeClass) -> typeClass.isAssignableFrom(clazz)))
                     .findAny();
    }
}
