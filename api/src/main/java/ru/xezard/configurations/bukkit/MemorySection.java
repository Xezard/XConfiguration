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
package ru.xezard.configurations.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static ru.xezard.configurations.bukkit.NumberConversions.*;

public class MemorySection
implements ConfigurationSection
{
    protected final Map<String, Object> map = new LinkedHashMap<> ();

    @Getter
    private final Configuration root;

    @Getter
    private final ConfigurationSection parent;

    private final String path,
                         fullPath;

    protected MemorySection()
    {
        if (!(this instanceof Configuration))
        {
            throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
        }

        this.path = "";
        this.fullPath = "";
        this.parent = null;
        this.root = (Configuration) this;
    }

    protected MemorySection(ConfigurationSection parent, String path)
    {
        Preconditions.checkNotNull(parent, "Parent cannot be null");
        Preconditions.checkNotNull(path, "Path cannot be null");

        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();

        Preconditions.checkNotNull(this.root, "Path cannot be orphaned");

        this.fullPath = createPath(parent, path);
    }

    public Set<String> getKeys(boolean deep)
    {
        Set<String> result = new LinkedHashSet<> ();

        if (this.root != null && this.root.options().isCopyDefaults())
        {
            ConfigurationSection defaults = this.getDefaultSection();

            if (defaults != null)
            {
                result.addAll(defaults.getKeys(deep));
            }
        }

        this.mapChildrenKeys(result, this, deep);

        return result;
    }

    public Map<String, Object> getValues(boolean deep)
    {
        Map<String, Object> result = new LinkedHashMap<> ();

        if (this.root != null && this.root.options().isCopyDefaults())
        {
            ConfigurationSection defaults = this.getDefaultSection();

            if (defaults != null)
            {
                result.putAll(defaults.getValues(deep));
            }
        }

        this.mapChildrenValues(result, this, deep);

        return result;
    }

    public boolean contains(String path)
    {
        return this.get(path) != null;
    }

    public boolean isSet(String path)
    {
        if (this.root == null)
        {
            return false;
        }

        if (this.root.options().isCopyDefaults())
        {
            return this.contains(path);
        }

        return this.get(path, null) != null;
    }

    public String getCurrentPath()
    {
        return this.fullPath;
    }

    public String getName()
    {
        return this.path;
    }

    public void addDefault(String path, Object value)
    {
        Preconditions.checkNotNull(path, "Path cannot be null");

        if (this.root == null)
        {
            throw new IllegalStateException("Cannot add default without root");
        }

        if (this.root == this)
        {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
        }

        this.root.addDefault(createPath(this, path), value);
    }

    public ConfigurationSection getDefaultSection()
    {
        Configuration defaults = this.root == null ? null : this.root.getDefaults();

        return defaults != null && defaults.isConfigurationSection(this.getCurrentPath()) ?
               defaults.getConfigurationSection(this.getCurrentPath()) : null;
    }

    public void set(String path, Object value)
    {
        Preconditions.checkArgument(!path.isEmpty(), "Cannot set to an empty path");

        if (this.root == null)
        {
            throw new IllegalStateException("Cannot use section without a root");
        }

        final char separator = this.root.options().getPathSeparator();

        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;

        ConfigurationSection section = this;

        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1)
        {
            String node = path.substring(i2, i1);

            ConfigurationSection subSection = section.getConfigurationSection(node);

            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);

        if (section == this)
        {
            if (value == null)
            {
                this.map.remove(key);
            } else {
                this.map.put(key, value);
            }
        } else {
            section.set(key, value);
        }
    }

    public Object get(String path)
    {
        return this.get(path, getDefault(path));
    }

    public Object get(String path, Object def)
    {
        Preconditions.checkNotNull(path, "Path cannot be null");

        if (path.length() == 0)
        {
            return this;
        }

        if (this.root == null)
        {
            throw new IllegalStateException("Cannot access section without a root");
        }

        final char separator = this.root.options().getPathSeparator();

        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;

        ConfigurationSection section = this;

        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1)
        {
            section = section.getConfigurationSection(path.substring(i2, i1));

            if (section == null)
            {
                return def;
            }
        }

        String key = path.substring(i2);

        if (section == this)
        {
            Object result = this.map.get(key);

            return (result == null) ? def : result;
        }

        return section.get(key, def);
    }

    public ConfigurationSection createSection(String path)
    {
        Preconditions.checkArgument(!path.isEmpty(), "Cannot create section at empty path");

        if (this.root == null)
        {
            throw new IllegalStateException("Cannot create section without a root");
        }

        final char separator = this.root.options().getPathSeparator();

        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;

        ConfigurationSection section = this;

        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1)
        {
            String node = path.substring(i2, i1);

            ConfigurationSection subSection = section.getConfigurationSection(node);

            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);

        if (section == this) {

            ConfigurationSection result = new MemorySection(this, key);

            this.map.put(key, result);

            return result;
        }

        return section.createSection(key);
    }

    public ConfigurationSection createSection(String path, Map<?, ?> map)
    {
        ConfigurationSection section = this.createSection(path);

        map.forEach((key, value) ->
        {
            if (value instanceof Map)
            {
                section.createSection(key.toString(), (Map<?, ?>) value);
            } else {
                section.set(key.toString(), value);
            }
        });

        return section;
    }

    public String getString(String path)
    {
        Object def = this.getDefault(path);

        return this.getString(path, def != null ? def.toString() : null);
    }

    public String getString(String path, String def)
    {
        Object val = this.get(path, def);

        return (val != null) ? val.toString() : def;
    }

    public boolean isString(String path)
    {
        return this.get(path) instanceof String;
    }

    public char getCharacter(String path)
    {
        Object def = this.getDefault(path);

        return this.getCharacter(path, (def instanceof Character) ? (char) def : 0);
    }

    public char getCharacter(String path, char def)
    {
        Object val = this.get(path, def);

        return (val instanceof Character) ? (char) val : def;
    }

    public boolean isCharacter(String path)
    {
        return this.get(path) instanceof Character;
    }

    public int getInt(String path)
    {
        Object def = this.getDefault(path);

        return getInt(path, (def instanceof Number) ? toInt(def) : 0);
    }

    public int getInt(String path, int def)
    {
        Object val = this.get(path, def);

        return (val instanceof Number) ? toInt(val) : def;
    }

    public boolean isInt(String path)
    {
        return this.get(path) instanceof Integer;
    }

    public boolean getBoolean(String path)
    {
        Object def = getDefault(path);

        return this.getBoolean(path, (def instanceof Boolean) ? (Boolean) def : false);
    }

    public boolean getBoolean(String path, boolean def)
    {
        Object val = this.get(path, def);

        return (val instanceof Boolean) ? (Boolean) val : def;
    }

    public boolean isBoolean(String path)
    {
        return this.get(path) instanceof Boolean;
    }

    public double getDouble(String path)
    {
        Object def = getDefault(path);

        return this.getDouble(path, (def instanceof Number) ? toDouble(def) : 0);
    }

    public double getDouble(String path, double def)
    {
        Object val = this.get(path, def);

        return (val instanceof Number) ? toDouble(val) : def;
    }

    public boolean isDouble(String path)
    {
        return this.get(path) instanceof Double;
    }

    public float getFloat(String path)
    {
        Object def = this.getDefault(path);

        return this.getFloat(path, (def instanceof Number) ? toFloat(def) : 0);
    }

    public float getFloat(String path, float def)
    {
        Object val = this.get(path, def);

        return (val instanceof Number) ? toFloat(val) : def;
    }

    public boolean isFloat(String path)
    {
        return this.get(path) instanceof Float;
    }

    public short getShort(String path)
    {
        Object def = this.getDefault(path);

        return this.getShort(path, (def instanceof Number) ? toShort(def) : 0);
    }

    public short getShort(String path, short def)
    {
        Object val = this.get(path, def);

        return (val instanceof Number) ? toShort(val) : def;
    }

    public boolean isShort(String path)
    {
        return this.get(path) instanceof Short;
    }

    public byte getByte(String path)
    {
        Object def = this.getDefault(path);

        return this.getByte(path, (def instanceof Number) ? toByte(def) : 0);
    }

    public byte getByte(String path, byte def)
    {
        Object val = this.get(path, def);

        return (val instanceof Number) ? toByte(val) : def;
    }

    public boolean isByte(String path)
    {
        return this.get(path) instanceof Byte;
    }

    public long getLong(String path)
    {
        Object def = this.getDefault(path);

        return this.getLong(path, (def instanceof Number) ? toLong(def) : 0);
    }

    public long getLong(String path, long def)
    {
        Object val = this.get(path, def);

        return (val instanceof Number) ? toLong(val) : def;
    }

    public boolean isLong(String path)
    {
        return this.get(path) instanceof Long;
    }

    public List<?> getList(String path)
    {
        Object def = this.getDefault(path);

        return this.getList(path, (def instanceof List) ? (List<?>) def : null);
    }

    public List<?> getList(String path, List<?> def)
    {
        Object val = this.get(path, def);

        return (List<?>) ((val instanceof List) ? val : def);
    }

    public boolean isList(String path)
    {
        return this.get(path) instanceof List;
    }

    public List<String> getStringList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null)
        {
            return new ArrayList<> ();
        }

        return list.stream()
                   .filter((object) -> object instanceof String || this.isPrimitiveWrapper(object))
                   .map(String::valueOf)
                   .collect(Collectors.toList());
    }

    public List<Integer> getIntegerList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null)
        {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Integer)
            {
                return (Integer) object;
            }

            if (object instanceof String)
            {
                return Integer.valueOf((String) object);
            }

            if (object instanceof Character)
            {
                return (int) (Character) object;
            }

            if (object instanceof Number)
            {
                return ((Number) object).intValue();
            }

            return 0;
        }).collect(Collectors.toList());
    }

    public List<Boolean> getBooleanList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null)
        {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Boolean)
            {
                return (Boolean) object;
            }

            if (object instanceof String)
            {
                return Boolean.parseBoolean((String) object);
            }

            return false;
        }).collect(Collectors.toList());
    }

    public List<Double> getDoubleList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null)
        {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Double)
            {
                return (Double) object;
            }

            if (object instanceof String)
            {
                return Double.valueOf((String) object);
            }

            if (object instanceof Character)
            {
                return (double) (Character) object;
            }

            if (object instanceof Number)
            {
                return ((Number) object).doubleValue();
            }

            return 0D;
        }).collect(Collectors.toList());
    }

    public List<Float> getFloatList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null) {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Float)
            {
                return (Float) object;
            }

            if (object instanceof String)
            {
                return Float.valueOf((String) object);
            }

            if (object instanceof Character)
            {
                return (float) (Character) object;
            }

            if (object instanceof Number)
            {
                return ((Number) object).floatValue();
            }

            return 0F;
        }).collect(Collectors.toList());
    }

    public List<Long> getLongList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null)
        {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Long)
            {
                return (Long) object;
            }

            if (object instanceof String)
            {
                return Long.valueOf((String) object);
            }

            if (object instanceof Character)
            {
                return (long) (Character) object;
            }

            if (object instanceof Number)
            {
                return ((Number) object).longValue();
            }

            return 0L;
        }).collect(Collectors.toList());
    }

    public List<Byte> getByteList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null) {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Byte)
            {
                return (Byte) object;
            }

            if (object instanceof String)
            {
                return Byte.valueOf((String) object);
            }

            if (object instanceof Character)
            {
                return (byte) ((Character) object).charValue();
            }

            if (object instanceof Number)
            {
                return ((Number) object).byteValue();
            }

            return (byte) 0;
        }).collect(Collectors.toList());
    }

    public List<Character> getCharacterList(String path)
    {
        List<?> list = this.getList(path);

        if (list == null) {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Character)
            {
                return (Character) object;
            }

            if (object instanceof String)
            {
                String string = (String) object;

                if (string.length() == 1)
                {
                    return string.charAt(0);
                }
            }

            if (object instanceof Number)
            {
                return (char) ((Number) object).intValue();
            }

            return 'a';
        }).collect(Collectors.toList());
    }

    public List<Short> getShortList(String path) {
        List<?> list = getList(path);

        if (list == null)
        {
            return new ArrayList<> ();
        }

        return list.stream().map((object) ->
        {
            if (object instanceof Short)
            {
                return (Short) object;
            }

            if (object instanceof String)
            {
                return Short.valueOf((String) object);
            }

            if (object instanceof Character)
            {
                return (short) ((Character) object).charValue();
            }

            if (object instanceof Number)
            {
                return ((Number) object).shortValue();
            }

            return (short) 0;
        }).collect(Collectors.toList());
    }

    public List<Map<?, ?>> getMapList(String path)
    {
        List<?> list = this.getList(path);

        List<Map<?, ?>> result = new ArrayList<> ();

        if (list == null)
        {
            return result;
        }

        return list.stream()
                   .filter((object) -> object instanceof Map)
                   .map((object) -> (Map<?, ?>) object)
                   .collect(Collectors.toList());
    }

    public ConfigurationSection getConfigurationSection(String path)
    {
        Object val = this.get(path, null);

        if (val != null)
        {
            return (val instanceof ConfigurationSection) ? (ConfigurationSection) val : null;
        }

        val = this.get(path, getDefault(path));

        return (val instanceof ConfigurationSection) ? this.createSection(path) : null;
    }

    public boolean isConfigurationSection(String path)
    {
        return this.get(path) instanceof ConfigurationSection;
    }

    protected boolean isPrimitiveWrapper(Object input)
    {
        return input instanceof Integer || input instanceof Boolean ||
               input instanceof Character || input instanceof Byte ||
               input instanceof Short || input instanceof Double ||
               input instanceof Long || input instanceof Float;
    }

    protected Object getDefault(String path)
    {
        Preconditions.checkNotNull(path, "Path cannot be null");

        Configuration defaults = this.root == null ? null : this.root.getDefaults();

        return (defaults == null) ? null : defaults.get(createPath(this, path));
    }

    protected void mapChildrenKeys(Set<String> output, ConfigurationSection section, boolean deep)
    {
        if (section instanceof MemorySection)
        {
            MemorySection sec = (MemorySection) section;

            sec.map.forEach((key, value) ->
            {
                output.add(createPath(section, key, this));

                if ((deep) && (value instanceof ConfigurationSection))
                {
                    this.mapChildrenKeys(output, (ConfigurationSection) value, deep);
                }
            });
            return;
        }

        Set<String> keys = section.getKeys(deep);

        for (String key : keys)
        {
            output.add(createPath(section, key, this));
        }
    }

    protected void mapChildrenValues(Map<String, Object> output, ConfigurationSection section, boolean deep)
    {
        if (section instanceof MemorySection)
        {
            MemorySection sec = (MemorySection) section;

            sec.map.forEach((key, value) ->
            {
                output.put(createPath(section, key, this), value);

                if (value instanceof ConfigurationSection && deep)
                {
                    this.mapChildrenValues(output, (ConfigurationSection) value, true);
                }
            });

            return;
        }

        Map<String, Object> values = section.getValues(deep);

        values.forEach((key, value) ->
        {
            output.put(createPath(section, key, this), value);
        });
    }

    public static String createPath(ConfigurationSection section, String key)
    {
        return createPath(section, key, (section == null) ? null : section.getRoot());
    }

    public static String createPath(ConfigurationSection section, String key, ConfigurationSection relativeTo)
    {
        Preconditions.checkNotNull(section, "Cannot create path without a section");

        Configuration root = section.getRoot();

        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root");
        }

        char separator = root.options().getPathSeparator();

        StringBuilder builder = new StringBuilder();

        for (ConfigurationSection parent = section; (parent != null) && (parent != relativeTo); parent = parent.getParent())
        {
            if (builder.length() > 0)
            {
                builder.insert(0, separator);
            }

            builder.insert(0, parent.getName());
        }

        if ((key != null) && (key.length() > 0))
        {
            if (builder.length() > 0)
            {
                builder.append(separator);
            }

            builder.append(key);
        }

        return builder.toString();
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() +
                "[path='" + getCurrentPath() +
                "', root='" + (this.root == null ? null : this.root.getClass().getSimpleName()) + "']";
    }
}