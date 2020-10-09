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
package ru.xezard.configurations.bukkit.serialization;

import com.google.common.base.Preconditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationSerialization 
{
    private static final Map<String, Class<? extends ConfigurationSerializable>> ALIASES = new HashMap<> ();

    private final Class<? extends ConfigurationSerializable> clazz;

    public static final String SERIALIZED_TYPE_KEY = "==";

    protected ConfigurationSerialization(Class<? extends ConfigurationSerializable> clazz)
    {
        this.clazz = clazz;
    }

    protected Method getMethod(String name, boolean isStatic)
    {
        try {
            Method method = this.clazz.getDeclaredMethod(name, Map.class);

            if (!ConfigurationSerializable.class.isAssignableFrom(method.getReturnType()) ||
                Modifier.isStatic(method.getModifiers()) != isStatic)
            {
                return null;
            }

            return method;
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

    protected Constructor<? extends ConfigurationSerializable> getConstructor()
    {
        try {
            return this.clazz.getConstructor(Map.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

    protected ConfigurationSerializable deserializeViaMethod(Method method, Map<String, ?> args)
    {
        try {
            ConfigurationSerializable result = (ConfigurationSerializable) method.invoke(null, args);

            if (result == null) {
                Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" +
                        method.toString() + "' of " + this.clazz + " for deserialization: method returned null");
            } else {
                return result;
            }
        } catch (Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName())
                  .log(Level.SEVERE,
                    "Could not call method '" + method.toString() + "' of " + this.clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    protected ConfigurationSerializable deserializeViaCtor(Constructor<? extends ConfigurationSerializable> ctor, Map<String, ?> args)
    {
        try {
            return ctor.newInstance(args);
        } catch (Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(
                    Level.SEVERE,
                    "Could not call constructor '" + ctor.toString() + "' of " + this.clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    public ConfigurationSerializable deserialize(Map<String, ?> args)
    {
        Preconditions.checkNotNull(args, "args must not be null");

        Method method = this.getMethod("deserialize", true);

        ConfigurationSerializable result = method != null ? this.deserializeViaMethod(method, args) : null;

        if (result == null)
        {
            method = this.getMethod("valueOf", true);

            if (method != null)
            {
                result = this.deserializeViaMethod(method, args);
            }
        }

        if (result == null)
        {
            Constructor<? extends ConfigurationSerializable> constructor = this.getConstructor();

            if (constructor != null)
            {
                result = this.deserializeViaCtor(constructor, args);
            }
        }

        return result;
    }

    public static ConfigurationSerializable deserializeObject(Map<String, ?> args, Class<? extends ConfigurationSerializable> clazz)
    {
        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    public static ConfigurationSerializable deserializeObject(Map<String, ?> args)
    {
        Class<? extends ConfigurationSerializable> clazz;

        if (args.containsKey(SERIALIZED_TYPE_KEY))
        {
            try {
                String alias = (String) args.get(SERIALIZED_TYPE_KEY);

                if (alias == null) {
                    throw new IllegalArgumentException("Cannot have null alias");
                }

                clazz = getClassByAlias(alias);

                if (clazz == null) {
                    throw new IllegalArgumentException("Specified class does not exist ('" + alias + "')");
                }
            } catch (ClassCastException ex) {
                ex.fillInStackTrace();
                throw ex;
            }
        } else {
            throw new IllegalArgumentException("Args doesn't contain type key ('" + SERIALIZED_TYPE_KEY + "')");
        }

        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    public static void registerClass(Class<? extends ConfigurationSerializable> clazz)
    {
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate == null)
        {
            registerClass(clazz, getAlias(clazz));
            registerClass(clazz, clazz.getName());
        }
    }

    public static void registerClass(Class<? extends ConfigurationSerializable> clazz, String alias)
    {
        ALIASES.put(alias, clazz);
    }

    public static void unregisterClass(String alias)
    {
        ALIASES.remove(alias);
    }

    public static void unregisterClass(Class<? extends ConfigurationSerializable> clazz)
    {
        ALIASES.values().remove(clazz);
    }

    public static Class<? extends ConfigurationSerializable> getClassByAlias(String alias)
    {
        return ALIASES.get(alias);
    }

    public static String getAlias(Class<? extends ConfigurationSerializable> clazz)
    {
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate != null && delegate.value() != clazz)
        {
            return getAlias(delegate.value());
        }

        SerializableAs alias = clazz.getAnnotation(SerializableAs.class);

        return alias != null ? alias.value() : clazz.getName();
    }
}