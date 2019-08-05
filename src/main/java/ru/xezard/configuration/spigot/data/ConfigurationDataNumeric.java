package ru.xezard.configuration.spigot.data;

public abstract class ConfigurationDataNumeric<T extends Number>
extends ConfigurationData<T>
{
    @Override
    public abstract T getDefault();
}