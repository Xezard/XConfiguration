package ru.xezard.configuration.spigot;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import ru.xezard.configuration.spigot.data.ConfigurationData;

@AllArgsConstructor
public abstract class Configuration
{
    private Plugin plugin;

    private String configurationName;

    public List<Field> getFields(Class<?> clazz)
    {
        Class parentClass = clazz.getSuperclass();

        List<Field> fields = Lists.newArrayList(clazz.getDeclaredFields());

        if (parentClass != null && parentClass != Object.class)
        {
            fields.addAll(this.getFields(parentClass));
        }

        return fields;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Configuration loadData(File file, boolean save)
    {
        YamlConfiguration configuration = new YamlConfiguration()
        {{
            this.load(file);
        }};

        boolean updated = false;

        for (Map.Entry<Field, SerializationOptions> fieldData : this.getFieldsData().entrySet())
        {
            Field field = fieldData.getKey();

            SerializationOptions serializationOptions = fieldData.getValue();

            boolean accessible = field.isAccessible();

            try {
                field.setAccessible(true);

                String path = serializationOptions.getPath();

                ConfigurationData configurationData = serializationOptions.getType().getDataType();

                Object configValue = configurationData.get(configuration, field.getType(), path, null);

                if (configValue == null)
                {
                    try {
                        configValue = field.get(this);

                        configurationData.set(configuration, path, configValue != null ? configValue : configurationData.getDefault());
                       
                        updated = true;
                        continue;
                    } catch (IllegalStateException | IllegalAccessException e) {
                        this.plugin.getLogger().warning("Could not get value to config file: " + configuration.getName() +
                                                                ", path: " + path);
                        e.printStackTrace();
                    }
                }

                try {
                    try {
                        field.set(this, configValue);
                    } catch (IllegalArgumentException e) {
                        field.set(this, null);
                    }

                    if (serializationOptions.getComment().length > 0) {} // Currently not worked
                } catch (IllegalAccessException e) {
                    this.plugin.getLogger().warning("Could not set value in configuration file: " + configuration.getName() +
                                                            ", path: " + path);
                    e.printStackTrace();
                }
            } finally {
                field.setAccessible(accessible);
            }
        }

        if (save && updated)
        {
            configuration.save(file);
        }

        return this;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Configuration saveData(File file)
    {
        YamlConfiguration configuration = new YamlConfiguration()
        {{
            this.load(file);
        }};

        boolean differs = false;

        for (Map.Entry<Field, SerializationOptions> fieldData : this.getFieldsData().entrySet())
        {
            Field field = fieldData.getKey();

            SerializationOptions serializationOptions = fieldData.getValue();

            String path = serializationOptions.getPath();

            ConfigurationData configurationData = serializationOptions.getType().getDataType();

            boolean accessible = field.isAccessible();

            try {
                field.setAccessible(true);

                Object fieldValue;

                try {
                    fieldValue = field.get(this);
                } catch (final IllegalStateException | IllegalAccessException e) {
                    this.plugin.getLogger().warning("Could not get value to config file: " + configuration.getName() +
                                                            ", path: " + path);
                    continue;
                }

                Object configValue = configurationData.get(configuration, field.getType(), serializationOptions.getPath());

                if (fieldValue != null && !fieldValue.equals(configValue)
                        || configValue != null && !configValue.equals(fieldValue))
                {
                    configurationData.set(configuration, serializationOptions.getPath(), fieldValue);

                    differs = true;
                }
            } finally {
                field.setAccessible(accessible);
            }
        }

        if (differs)
        {
            configuration.save(file);
        }

        return this;
    }

    public Map<Field, SerializationOptions> getFieldsData()
    {
        Map<Field, SerializationOptions> fieldsData = new HashMap<> ();

        Class<? extends Configuration> thisClass = this.getClass();

        for (Field field : this.getFields(thisClass))
        {
            if (field.isAnnotationPresent(ConfigurationField.class))
            {
                ConfigurationField data = field.getAnnotation(ConfigurationField.class);

                ConfigurationType.getType(field).ifPresentOrElse((type) ->
                {
                    fieldsData.put
                    (
                            field,

                            SerializationOptions.of
                            (
                                    type,
                                    data.value().isEmpty() ? field.getName() : data.value(),
                                    data.comment()
                            )
                    );
                }, () -> this.plugin.getLogger().warning("Cannot find type of field: " + field.getName()));
            } else if (field.getDeclaringClass() != thisClass && this.isModifiable(field.getModifiers())) {
                ConfigurationType.getType(field).ifPresentOrElse((type) -> {
                    fieldsData.put(field, SerializationOptions.of(type, field.getName(), new String[0]));
                }, () -> this.plugin.getLogger().warning("Cannot find type of field: " + field.getName()));
            }
        }

        return fieldsData;
    }

    @SneakyThrows
    public Configuration load(File file, boolean save)
    {
        File parent = file.getParentFile();

        if (!parent.isDirectory())
        {
            Files.createDirectory(parent.toPath());
        }

        if (!file.isFile())
        {
            Files.createFile(file.toPath());
        }

        return this.loadData(file, save);
    }

    public Configuration load(String path, boolean save)
    {
        return this.load(new File(this.plugin.getDataFolder(), path), save);
    }

    public Configuration load(boolean save)
    {
        return this.load(this.configurationName, save);
    }

    @SneakyThrows
    public Configuration load(File file)
    {
        File parent = file.getParentFile();

        if (!parent.isDirectory())
        {
            Files.createDirectory(parent.toPath());
        }

        if (!file.isFile())
        {
            Files.createFile(file.toPath());
        }

        return this.loadData(file, true);
    }

    public Configuration load(String path)
    {
        return this.load(new File(this.plugin.getDataFolder(), path));
    }

    public Configuration load()
    {
        return this.load(this.configurationName);
    }

    @SneakyThrows
    public Configuration save(File file)
    {
        File parent = file.getParentFile();

        if (!parent.isDirectory())
        {
            Files.createDirectory(parent.toPath());
        }

        if (!file.isFile())
        {
            Files.createFile(file.toPath());
        }

        return this.saveData(file);
    }

    public Configuration save(String path)
    {
        return this.save(new File(this.plugin.getDataFolder(), path));
    }

    public Configuration save()
    {
        return this.save(this.configurationName);
    }

    public Configuration copyFrom(Configuration configuration)
    {
        Class thisClass = this.getClass();

        for (Field field : this.getFields(thisClass))
        {
            if (field.getDeclaringClass() != thisClass &&
                this.isModifiable(field.getModifiers()) || field.isAnnotationPresent(ConfigurationField.class))
            {
                boolean accessible = field.isAccessible();

                try {
                    field.setAccessible(true);

                    field.set(this, field.get(configuration));
                } catch (final IllegalAccessException e) {
                    this.plugin.getLogger().warning("Could not copy value from one configuration object to another: ");
                    e.printStackTrace();
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }

        return this;
    }

    public boolean isModifiable(int modifiers)
    {
        return (modifiers & Modifier.STATIC) == 0
                && (modifiers & Modifier.FINAL) == 0
                && (modifiers & Modifier.TRANSIENT) == 0;
    }
}
