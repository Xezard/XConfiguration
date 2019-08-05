package ru.xezard.configuration.spigot;

import lombok.Value;

@Value(staticConstructor = "of")
class SerializationOptions
{
    private ConfigurationType type;

    private String path;

    private String[] comment;
}