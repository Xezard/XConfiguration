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

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import ru.xezard.configuration.spigot.data.types.AbstractConfigurationData;

@AllArgsConstructor
public class Configuration
{
    private Plugin plugin;

    private String configurationName;

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Configuration loadData(File file, boolean save)
    {
        Multimap<String, String> comments = ArrayListMultimap.create();

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

                AbstractConfigurationData configurationData = serializationOptions.getData();

                Comment[] fieldComments = serializationOptions.getComments();

                Object configValue = configuration.get(path);

                if (fieldComments.length > 0)
                {
                    for (Comment comment : fieldComments)
                    {
                        String commentPath = comment.path();

                        if (commentPath.isEmpty() || !path.contains(commentPath))
                        {
                            continue;
                        }

                        comments.putAll(commentPath, Arrays.asList(comment.comments()));
                    }
                }

                if (configValue == null || configValue.getClass() != field.getType())
                {
                    try {
                        configValue = field.get(this);

                        configurationData.set(configuration, path, configValue != null ?
                                              configValue : configurationData.getDefault(), field.getType());

                        updated = true;
                        continue;
                    } catch (IllegalStateException | IllegalAccessException e) {
                        this.plugin.getLogger().warning("Could not get value from field: " + configuration.getName() +
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
                } catch (IllegalAccessException e) {
                    this.plugin.getLogger().warning("Could not set value in field from configuration file: " + configuration.getName() +
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

        if (!comments.isEmpty())
        {
            List<String> lines = new ArrayList<> ();

            @Cleanup
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();

                comments.forEach((path, comment) ->
                {
                    if (!line.contains(path))
                    {
                        return;
                    }

                    String trimmed = comment.trim();

                    if (trimmed.startsWith("#") || trimmed.isEmpty())
                    {
                        lines.add(comment);
                    }
                });

                lines.add(line);
            }

            @Cleanup
            PrintWriter printWriter = new PrintWriter(file);

            lines.forEach(printWriter::println);
        }

        return this;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Configuration saveData(File file)
    {
        YamlConfiguration configuration = new YamlConfiguration()
        {{
            this.load(file);
        }};

        boolean hasChanges = false;

        for (Map.Entry<Field, SerializationOptions> fieldData : this.getFieldsData().entrySet())
        {
            Field field = fieldData.getKey();

            SerializationOptions serializationOptions = fieldData.getValue();

            String path = serializationOptions.getPath();

            AbstractConfigurationData configurationData = serializationOptions.getData();

            boolean accessible = field.isAccessible();

            try {
                field.setAccessible(true);

                Object fieldValue;

                try {
                    fieldValue = field.get(this);
                } catch (IllegalStateException | IllegalAccessException e) {
                    this.plugin.getLogger().warning("Could not get value from config file: " +
                                                    configuration.getName() + ", path: " + path);
                    continue;
                }

                Object configValue = configurationData.get(configuration, path, field.getType());

                if (fieldValue != null && !fieldValue.equals(configValue) ||
                    configValue != null && !configValue.equals(fieldValue))
                {
                    configurationData.set(configuration, path, fieldValue, field.getType());

                    hasChanges = true;
                }
            } finally {
                field.setAccessible(accessible);
            }
        }

        if (hasChanges)
        {
            configuration.save(file);
        }

        return this;
    }

    private Map<Field, SerializationOptions> getFieldsData()
    {
        LinkedHashMap<Field, SerializationOptions> fieldsData = new LinkedHashMap<> ();

        Class<? extends Configuration> thisClass = this.getClass();

        this.getFields(thisClass)
            .stream()
            .filter((field) -> field.isAnnotationPresent(ConfigurationField.class))
            .forEach((field) ->
        {
            ConfigurationField data = field.getAnnotation(ConfigurationField.class);

            Optional<AbstractConfigurationData> optionalConfigurationData = ConfigurationManager.getType(field);

            if (optionalConfigurationData.isPresent())
            {
                AbstractConfigurationData configurationData = optionalConfigurationData.get();

                fieldsData.put(field, SerializationOptions.builder()
                                                          .data(configurationData)
                                                          .path(data.value().isEmpty() ? field.getName() : data.value())
                                                          .comments(data.comments())
                                                          .build());
            } else {
                this.plugin.getLogger().warning("Cannot find configuration data for field: " + field.getName());
            }
        });

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
            if (field.getDeclaringClass() == thisClass || !field.isAnnotationPresent(ConfigurationField.class))
            {
                continue;
            }

            boolean accessible = field.isAccessible();

            try {
                field.setAccessible(true);

                field.set(this, field.get(configuration));
            } catch (IllegalAccessException e) {
                this.plugin.getLogger().warning("Could not copy value from one configuration object to another: ");
                e.printStackTrace();
            } finally {
                field.setAccessible(accessible);
            }
        }

        return this;
    }

    private List<Field> getFields(Class<?> clazz)
    {
        return Arrays.asList(clazz.getDeclaredFields());
    }
}