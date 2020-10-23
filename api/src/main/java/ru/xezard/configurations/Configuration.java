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
package ru.xezard.configurations;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.xezard.configurations.bukkit.file.YamlConfiguration;
import ru.xezard.configurations.data.types.AbstractConfigurationData;
import ru.xezard.configurations.manager.ConfigurationsDataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

@AllArgsConstructor
public class Configuration
implements IConfiguration
{
    private static Logger logger = Logger.getLogger(Configuration.class.getName());

    @Getter
    protected String pathToFile;

    @SneakyThrows
    private Configuration loadData(File file, boolean save)
    {
        Multimap<String, String> comments = MultimapBuilder.hashKeys().arrayListValues().build();

        YamlConfiguration configuration = new YamlConfiguration();

        configuration.load(file);

        boolean updated = false;

        ConfigurationComments header = this.getClass().getAnnotation(ConfigurationComments.class);

        if (header != null)
        {
            configuration.options().header(Joiner.on("\r\n").join(header.value()));
        }

        for (Map.Entry<Field, SerializationOptions> fieldData : this.getFieldsData().entrySet())
        {
            Field field = fieldData.getKey();

            SerializationOptions serializationOptions = fieldData.getValue();

            AbstractConfigurationData configurationData = serializationOptions.getData();

            String path = serializationOptions.getPath();

            String[] fieldComments = serializationOptions.getComments();

            if (fieldComments != null)
            {
                comments.putAll(path, Arrays.asList(fieldComments));
            }

            boolean accessible = field.isAccessible();

            try {
                field.setAccessible(true);

                Object configValue = configurationData.get(configuration, path, field);

                if (configValue == null)
                {
                    try {
                        configValue = field.get(this);

                        configurationData.set(configuration, path, configValue != null ?
                                configValue : configurationData.getDefault(), field);

                        updated = true;
                    } catch (Exception e) {
                        logger.warning("Could not get value from field '" + field.getName() +
                                "', path '" + path + "' and set it to configuration!");
                        e.printStackTrace();
                    }

                    continue;
                }

                try {
                    field.set(this, configValue);
                } catch (Exception e) {
                    logger.warning("Could not set value in field from configuration file, path to file '" +
                            this.pathToFile + "', path in configuration '" + path + "'!");
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

        this.addCommentsToFile(comments, configuration, file);
        return this;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Configuration saveData(File file)
    {
        Multimap<String, String> comments = MultimapBuilder.hashKeys().arrayListValues().build();

        YamlConfiguration configuration = new YamlConfiguration();

        configuration.load(file);

        boolean hasChanges = false;

        ConfigurationComments header = this.getClass().getAnnotation(ConfigurationComments.class);

        if (header != null)
        {
            configuration.options().header(Joiner.on("\r\n").join(header.value()));
        }

        for (Map.Entry<Field, SerializationOptions> fieldData : this.getFieldsData().entrySet())
        {
            Field field = fieldData.getKey();

            SerializationOptions serializationOptions = fieldData.getValue();

            String path = serializationOptions.getPath();

            AbstractConfigurationData configurationData = serializationOptions.getData();

            String[] fieldComments = serializationOptions.getComments();

            if (fieldComments != null)
            {
                comments.putAll(path, Arrays.asList(fieldComments));
            }

            boolean accessible = field.isAccessible();

            try {
                field.setAccessible(true);

                Object fieldValue;

                try {
                    fieldValue = field.get(this);
                } catch (IllegalStateException | IllegalAccessException e) {
                    logger.warning("Could not get value from configuration class: " +
                            this.getClass().getName() + ", path: " + path);
                    continue;
                }

                Object configValue = configurationData.get(configuration, path, field);

                if (fieldValue != null && !fieldValue.equals(configValue) ||
                    configValue != null && !configValue.equals(fieldValue))
                {
                    configurationData.set(configuration, path, fieldValue, field);

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

        this.addCommentsToFile(comments, configuration, file);
        return this;
    }

    private LinkedHashMap<Field, SerializationOptions> getFieldsData()
    {
        LinkedHashMap<Field, SerializationOptions> fieldsData = new LinkedHashMap<> ();

        Stream.of(this.getClass().getDeclaredFields())
              .filter((field) -> field.isAnnotationPresent(ConfigurationField.class))
              .forEach((field) ->
              {
                  ConfigurationField data = field.getAnnotation(ConfigurationField.class);
                  ConfigurationComments comments = field.getAnnotation(ConfigurationComments.class);

                  Optional<AbstractConfigurationData> optionalConfigurationData = ConfigurationsDataManager.getInstance().getType(field);

                  if (optionalConfigurationData.isPresent())
                  {
                      SerializationOptions.SerializationOptionsBuilder serializationOptions =
                              SerializationOptions.builder()
                                                  .data(optionalConfigurationData.get())
                                                  .path(data.value().isEmpty() ? field.getName() : data.value());

                      if (comments != null)
                      {
                          serializationOptions.comments(comments.value());
                      }

                      fieldsData.put(field, serializationOptions.build());
                  } else {
                      logger.warning("Can't find configuration data for field: " + field.getName());
                  }
              });

        return fieldsData;
    }

    /*
     * This is not fully tested yet
     * and there is probably a better
     * way to write comments to the yaml
     * file, but at least it works.
     */
    private void addCommentsToFile(Multimap<String, String> comments, YamlConfiguration configuration, File file)
    {
        if (comments.isEmpty())
        {
            return;
        }

        int configurationIndent = configuration.options().getIndent();

        YamlEffectiveModel yamlEffectiveModel = new YamlEffectiveModel();

        List<String> lines = new ArrayList<> ();

        try (Scanner scanner = new Scanner(file))
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();

                if (yamlEffectiveModel.isKey(line))
                {
                    long lineIndent = yamlEffectiveModel.getIndent(line) / configurationIndent,
                         currentIndent = yamlEffectiveModel.getCurrentIndent();

                    String key = yamlEffectiveModel.toKey(line);

                    if (currentIndent == lineIndent)
                    {
                        if (yamlEffectiveModel.size() == 0)
                        {
                            yamlEffectiveModel.addKey(key);
                        } else {
                            yamlEffectiveModel.replaceCurrentKey(key);
                        }
                    } else if (currentIndent < lineIndent) {
                        yamlEffectiveModel.addKey(key);
                    } else {
                        yamlEffectiveModel.removeKeys((currentIndent - lineIndent) + 1);
                        yamlEffectiveModel.addKey(key);
                    }

                    yamlEffectiveModel.setCurrentIndent(lineIndent);
                } else {
                    comments.values().removeIf(line::contains);
                }

                String currentPath = yamlEffectiveModel.getCurrentPath();

                Iterator<Map.Entry<String, String>> commentsEntries = comments.entries().iterator();

                while (commentsEntries.hasNext())
                {
                    Map.Entry<String, String> commentEntry = commentsEntries.next();

                    if (!currentPath.equals(commentEntry.getKey()))
                    {
                        break;
                    }

                    String comment = commentEntry.getValue(),
                           trimmed = comment.startsWith("#") ? comment.trim() : "#" + comment.trim();

                    lines.add(Strings.repeat
                    (
                            " ",
                            ((int) yamlEffectiveModel.getCurrentIndent() * configurationIndent)
                    ) + trimmed);

                    commentsEntries.remove();
                }

                lines.add(line);
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        try (PrintWriter printWriter = new PrintWriter(file))
        {
            lines.forEach(printWriter::println);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private Configuration load(String path, boolean save)
    {
        return this.load(new File(path), save);
    }

    private Configuration load(String path)
    {
        return this.load(new File(path));
    }

    private Configuration save(String path)
    {
        return this.save(new File(path));
    }

    @SneakyThrows
    private Configuration load(File file, boolean save)
    {
        this.createFile(file);

        return this.loadData(file, save);
    }

    @Override
    public Configuration load(boolean save)
    {
        return this.load(this.pathToFile, save);
    }

    @SneakyThrows
    private Configuration load(File file)
    {
        return this.load(file, true);
    }

    @Override
    public IConfiguration load()
    {
        return this.load(this.pathToFile);
    }

    @SneakyThrows
    private Configuration save(File file)
    {
        this.createFile(file);

        return this.saveData(file);
    }

    @Override
    public Configuration save()
    {
        return this.save(this.pathToFile);
    }

    @SneakyThrows
    private void createFile(File file)
    {
        if (file.exists())
        {
            return;
        }

        file.getParentFile().mkdirs();
        file.createNewFile();
    }
}